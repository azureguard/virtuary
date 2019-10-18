const functions = require("firebase-functions");
const algoliasearch = require("algoliasearch");

const ALGOLIA_ID = functions.config().algolia.app_id;
const ALGOLIA_ADMIN_KEY = functions.config().algolia.api_key;
const ALGOLIA_SEARCH_KEY = functions.config().algolia.search_key;

const ALGOLIA_INDEX_NAME = "item_name";
const client = algoliasearch(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);

exports.onItemCreated = (snap, context) => {
  // Get the document
  const item = snap.data();

  // Add an 'objectID' field which Algolia requires
  item.objectID = context.params.itemId;

  // Write to the algolia index
  const index = client.initIndex(ALGOLIA_INDEX_NAME);
  return index.saveObject(item);
};
