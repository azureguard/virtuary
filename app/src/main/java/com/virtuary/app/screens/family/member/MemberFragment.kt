package com.virtuary.app.screens.family.member

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.virtuary.app.MainActivityViewModel
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyMemberBinding
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item
import com.virtuary.app.firebase.StorageRepository
import com.virtuary.app.util.GlideApp
import kotlinx.android.synthetic.main.dialog_edit_profile.view.*

/**
 * Fragment for the family member details
 */
class MemberFragment : Fragment() {

    // argument got from navigation action
    private val args: MemberFragmentArgs by navArgs()
    private val repository: FirestoreRepository = FirestoreRepository()
    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>()
    private val viewModel by viewModels<MemberViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentFamilyMemberBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_family_member, container, false
        )

        // To enable the option menu for set alias
        setHasOptionsMenu(true)

        // Set the name by the argument passed from navigation
        if (args.user.alias != null && args.user.alias!!.containsKey(mainActivityViewModel.currentUser)) {
            val name = args.user.alias!![mainActivityViewModel.currentUser]
            binding.memberName.text = name
            viewModel.name.value = name
        } else {
            binding.memberName.text = args.user.name
            viewModel.name.value = args.user.name
        }

        GlideApp.with(this)
            .load(StorageRepository().getImage(args.user.image))
            .placeholder(R.drawable.ic_no_image)
            .centerCrop()
            .into(binding.memberPicture)

        val userItems = args.user.item

        if (userItems == null || userItems.keys.isEmpty()) {
            binding.noItemText.visibility = View.VISIBLE
            binding.rvMemberItemList.visibility = View.GONE
            binding.showAllButton.visibility = View.GONE
        } else {
            binding.noItemText.visibility = View.GONE
            binding.rvMemberItemList.visibility = View.VISIBLE
            binding.showAllButton.visibility = View.VISIBLE
        }

        val userItem = mutableListOf<Item>()
        var itemCount = 0
        if (userItems != null) {
            for (item in userItems.values) {
                itemCount++
                userItem.add(item)
                if (itemCount == 5) {
                    break
                }
            }
        }

        // assign adapter so all item list behave the same way
        val adapter = MemberItemAdapter(::relatedItemOnClick, this)
        binding.rvMemberItemList.adapter = adapter
        adapter.submitList(userItem)

        binding.rvMemberItemList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        binding.showAllButton.setOnClickListener {
            findNavController().navigate(
                MemberFragmentDirections.actionMemberFragmentToMemberItemFragment(
                    args.user
                )
            )
        }

        viewModel.name.observe(this, Observer {
            binding.memberName.text = it
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_alias -> {
                createDialog(context!!, getString(R.string.set_alias))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun relatedItemOnClick(item: Item) {
        findNavController().navigate(
            MemberFragmentDirections.actionGlobalItemFragment(
                item
            )
        )
    }

    private fun createDialog(context: Context, title: String) {
        val builder = MaterialAlertDialogBuilder(context)
        val viewInflated = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_profile, view as ViewGroup?, false)

        // Set up the input
        val input = viewInflated.findViewById(R.id.input) as EditText
        input.text = SpannableStringBuilder(viewModel.name.value)

        // Create the alert dialog
        builder.apply {
            setTitle(title)
            setView(viewInflated)
            setPositiveButton(android.R.string.ok, null)
            setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ -> dialog.cancel() }
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val button = dialog.getButton(Dialog.BUTTON_POSITIVE)
            viewInflated.input.inputType =
                InputType.TYPE_TEXT_VARIATION_PERSON_NAME or InputType.TYPE_TEXT_FLAG_CAP_WORDS

            button.setOnClickListener {
                button.isEnabled = false
                val alias = input.text.toString()
                repository.updateUserAlias(
                    args.user.documentId!!,
                    mainActivityViewModel.currentUser,
                    alias
                )
                viewModel.name.value = alias
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}
