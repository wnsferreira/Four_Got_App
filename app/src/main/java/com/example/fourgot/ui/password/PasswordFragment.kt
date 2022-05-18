package com.example.fourgot.ui.password

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.fourgot.R
import com.example.fourgot.data.db.AppDatabase
import com.example.fourgot.data.db.dao.VaultDAO
import com.example.fourgot.extension.hideKeyboard
import com.example.fourgot.repository.DatabaseDataSource
import com.example.fourgot.repository.VaultRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_password.*


class PasswordFragment : Fragment(R.layout.fragment_password) {

    private val viewModel: PasswordViewModel by viewModels {
       object :ViewModelProvider.Factory {
           override fun <T : ViewModel> create(modelClass: Class<T>): T {
               val vaultDAO: VaultDAO =
                   AppDatabase.getInstance(requireContext()).vaultDAO

               val repository: VaultRepository = DatabaseDataSource(vaultDAO)
               return  PasswordViewModel(repository) as T
           }
       }
    }

    companion object {
        fun newInstance() = PasswordFragment()
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_password, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvents()
        setListeners()
    }



    private fun observeEvents() {

        viewModel.passwordStateEventData.observe(viewLifecycleOwner){ passwordState ->
            when (passwordState) {
                is PasswordViewModel.PasswordState.Inserted -> {
                    clearFields()
                    hideKeyboard()
                }
            }

            viewModel.messageEventData.observe(viewLifecycleOwner){ stringResId ->
                Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun clearFields() {

        input_name.text?.clear()
        input_password.text?.clear()
    }

// Força o fechamento do teclado
    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity){
            parentActivity.hideKeyboard()
        }
    }

//    Envia os dados da view para a viewModel
    private fun setListeners() {

        button_password.setOnClickListener{
            val name = input_name.text.toString()
            val password = input_password.text.toString()

            viewModel.addPassword(name, password)
        }
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(PasswordViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}