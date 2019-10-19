const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const path = require("path");
const sharp = require("sharp");

const THUMB_MAX_WIDTH = 200;
const THUMB_MAX_HEIGHT = 200;

/**
 * When an image is uploaded in the Storage bucket We generate a thumbnail automatically using
 * Sharp.
 */
exports.generateThumbnail = functions.storage.object().onFinalize(object => {
  const fileBucket = object.bucket; // The Storage bucket that contains the file.
  const filePath = object.name; // File path in the bucket.
  const contentType = object.contentType; // File content type.

  // Exit if this is triggered on a file that is not an image.
  if (!contentType.startsWith("image/")) {
    console.log("This is not an image.");
    return null;
  }

  // Get the file name.
  const fileName = path.basename(filePath);
  // Exit if the image is already a thumbnail.
  if (fileName.startsWith("thumb_")) {
    console.log("Already a Thumbnail.");
    return null;
  }

  // Download file from bucket.
  const bucket = admin.storage().bucket(fileBucket);

  const metadata = {
    contentType: contentType
  };
  // We add a 'thumb_' prefix to thumbnails file name. That's where we'll upload the thumbnail.
  const thumbFileName = `thumb_${fileName}`;
  const thumbFilePath = path.join(path.dirname(filePath), thumbFileName);
  // Create write stream for uploading thumbnail
  const thumbnailUploadStream = bucket
    .file(thumbFilePath)
    .createWriteStream({ metadata });

  // Create Sharp pipeline for resizing the image and use pipe to read from bucket read stream
  const pipeline = sharp();
  pipeline
    .resize(THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT)
    .pipe(thumbnailUploadStream);

  bucket
    .file(filePath)
    .createReadStream()
    .pipe(pipeline);

  return new Promise((resolve, reject) =>
    thumbnailUploadStream.on("finish", resolve).on("error", reject)
  );
});

exports.updateItemAssociatedWithUser = functions.firestore
  .document("Item/{documentId}")
  .onWrite(async (change, context) => {
    const docId = context.params.documentId;
    // If data is null, item deleted
    const currentData = change.after.exists ? change.after.data() : null;
    const oldData = change.before.exists ? change.before.data() : null;
    const promiseArray = [];
    let oldRelatedUsersId = [];

    // Get old related to for updated items
    if (oldData !== null) {
      oldRelatedUsersId = oldData.relations;
    }

    // If item deleted
    if (currentData === null) {
      promiseArray.push(
        oldRelatedUsersId.map(val => {
          const user = admin
            .firestore()
            .collection("User")
            .doc(val);
          return user.get().then(document => {
            const currentUser = document.data();
            delete currentUser.item[docId];
            return user.set(currentUser);
          });
        })
      );
      return Promise.all(promiseArray);
    }

    // Get new related to
    const relatedUsersId = currentData.relations;

    // Get removed users related to
    const removedRelations = oldRelatedUsersId.filter(
      val => !relatedUsersId.includes(val)
    );

    promiseArray.push(
      relatedUsersId.map(val => {
        const user = admin
          .firestore()
          .collection("User")
          .doc(val);
        return user.get().then(document => {
          const currentUser = document.data();
          if (currentUser.item === null) { currentUser.item = {}; }
          currentUser.item[docId] = currentData;
          return user.set(currentUser);
        });
      })
    );

    promiseArray.push(
      removedRelations.map(val => {
        const user = admin
          .firestore()
          .collection("User")
          .doc(val);
        return user.get().then(document => {
          const currentUser = document.data();
          delete currentUser.item[docId];
          return user.set(currentUser);
        });
      })
    );

    return Promise.all(promiseArray);
  });
