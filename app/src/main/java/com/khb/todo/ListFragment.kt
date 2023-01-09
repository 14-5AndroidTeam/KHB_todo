package com.khb.todo

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.khb.todo.Data.DTO.User
import com.khb.todo.ViewModel.ListModel
import com.khb.todo.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ListviewAdapter
    private lateinit var user: User

    override fun onAttach(context: Context) {
        super.onAttach(context)
        user = (context as FragmentActivity).user
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            onViewStateRestored(savedInstanceState)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.button.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.back_frag, DialogFragment())
                .commit()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        adapter = ListviewAdapter(requireContext())
        binding.listView.adapter = adapter
        adapter.registerDataSetObserver(object: DataSetObserver(){
            override fun onChanged() {
                binding.txtEmpty.visibility = if(adapter.count == 0) View.VISIBLE else View.INVISIBLE
            }
        })

        val viewModel = ListModel()
        viewModel.getList(user)
        viewModel.setList.observe(viewLifecycleOwner) {
            adapter.setData(it.peekContent())
            adapter.notifyDataSetChanged()
        }
    }
}