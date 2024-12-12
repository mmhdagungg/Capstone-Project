package com.kamalapp.cashify.ui.insert

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.response.InputData
import com.kamalapp.cashify.databinding.FragmentInsertBinding

class InsertFragment : Fragment() {

    private var _binding: FragmentInsertBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", 0)
        val token = sharedPref.getString("TOKEN", null)

        binding.btnGenerate.setOnClickListener {
            try {
                val labaKotor = binding.edLaba.text.toString().toIntOrNull() ?: 0
                val bayarGaji = binding.edGaji.text.toString().toIntOrNull() ?: 0
                val bayarAir = binding.edAir.text.toString().toIntOrNull() ?: 0
                val biayaListrik = binding.edListrik.text.toString().toIntOrNull() ?: 0
                val biayaTransport = binding.edTransport.text.toString().toIntOrNull() ?: 0
                val biayaPromosi = binding.edPromosi.text.toString().toIntOrNull() ?: 0
                val biayaPackaging = binding.edPackaging.text.toString().toIntOrNull() ?: 0
                val biayaPajak = binding.edPajak.text.toString().toIntOrNull() ?: 0

                if (token == null) {
                    Toast.makeText(requireContext(), "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val inputData = InputData(
                    labaKotor = labaKotor,
                    bayarGaji = bayarGaji,
                    bayarAir = bayarAir,
                    biayaListrik = biayaListrik,
                    biayaTransport = biayaTransport,
                    biayaPromosi = biayaPromosi,
                    biayaPackaging = biayaPackaging,
                    biayaPajak = biayaPajak
                )
                Log.d("InsertFragment", "Data yang akan dikirim: $inputData")

                val insertViewModel = ViewModelProvider(this)[InsertViewModel::class.java]
                insertViewModel.inputData(token, userId, inputData)
                insertViewModel.responseMessage.observe(viewLifecycleOwner) { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    if (message.contains("berhasil", ignoreCase = true)) {
                        findNavController().navigate(R.id.action_insertFragment_to_homeFragment)
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Input tidak valid. Pastikan semua nilai adalah angka.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
