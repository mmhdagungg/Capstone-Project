package com.kamalapp.cashify.ui.insert

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kamalapp.cashify.data.response.InputData
import com.kamalapp.cashify.databinding.FragmentInsertBinding
import java.util.*

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
        // DatePicker untuk memilih tanggal
        binding.edTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    binding.edTanggal.setText(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }


        binding.btnGenerate.setOnClickListener {
            val inputData = InputData(
                labaKotor = binding.edLaba.text.toString().ifEmpty { "0" },
                bayarGaji = binding.edGaji.text.toString().ifEmpty { "0" },
                bayarAir = binding.edAir.text.toString().ifEmpty { "0" },
                biayaListrik = binding.edListrik.text.toString().ifEmpty { "0" },
                biayaTransport = binding.edTransport.text.toString().ifEmpty { "0" },
                biayaPromosi = binding.edPromosi.text.toString().ifEmpty { "0" },
                biayaPackaging = binding.edPackaging.text.toString().ifEmpty { "0" },
                biayaPajak = binding.edPajak.text.toString().ifEmpty { "0" }
            )

            val insertViewModel = ViewModelProvider(this)[InsertViewModel::class.java]
            if (token != null) {
                insertViewModel.inputData(token, userId, inputData)
            }

            insertViewModel.responseMessage.observe(viewLifecycleOwner) { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
