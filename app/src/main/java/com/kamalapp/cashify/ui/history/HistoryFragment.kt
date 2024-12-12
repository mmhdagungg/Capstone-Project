package com.kamalapp.cashify.ui.history

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.HistoryItem
import com.kamalapp.cashify.databinding.FragmentHistoryBinding
import com.kamalapp.cashify.ui.detailHistory.DetailHistoryActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        val adapter = HistoryAdapter(emptyList()) { historyItem ->
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java).apply {
                putExtra("id_profile", historyItem.idProfile)
                putExtra("id_data", historyItem.idData)
                putExtra("labaKotor", historyItem.labaKotor)
                putExtra("bayarGaji", historyItem.bayarGaji)
                putExtra("bayarAir", historyItem.bayarAir)
                putExtra("biayaListrik", historyItem.biayaListrik)
                putExtra("biayaTransport", historyItem.biayaTransport)
                putExtra("biayaPromosi", historyItem.biayaPromosi)
                putExtra("biayaPackaging", historyItem.biayaPackaging)
                putExtra("biayaPajak", historyItem.biayaPajak)
                putExtra("date", historyItem.date)
            }
            startActivity(intent)
        }

        binding.rvHistory.adapter = adapter

        val sharedPref =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", 0)
        val token = sharedPref.getString("TOKEN", null)

        // Atur default tanggal ke bulan dan tahun saat ini
        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate.time)
        binding.edTanggal.setText(SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentDate.time)) // Format dengan nama bulan
        loadHistoryData(adapter, token, userId, formattedDate)

        binding.edTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, _ ->
                    // Set bulan dan tahun yang dipilih, tidak ada tanggal
                    calendar.set(year, month, 1)
                    val newFormattedDate = dateFormat.format(calendar.time)
                    binding.edTanggal.setText(
                        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
                    )
                    loadHistoryData(adapter, token, userId, newFormattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                // Sembunyikan tampilan pemilihan hari
                datePicker.findViewById<View>(resources.getIdentifier("android:id/day", null, null))?.visibility = View.GONE
            }.show()
        }
    }

    private fun loadHistoryData(
        adapter: HistoryAdapter,
        token: String?,
        userId: Int,
        dateTime: String?
    ) {
        token?.let {
            viewModel.getHistory(it, userId, dateTime ?: "").observe(viewLifecycleOwner) { response ->
                val mappedItems = response?.data?.map { historyData ->
                    // Format date to dd-MM-yyyy
                    val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(historyData.dateAdded ?: "1970-01-01")
                    )

                    HistoryItem(
                        idProfile = historyData.idProfile ?: 0,
                        idData = historyData.id ?: 0,
                        title = "Hasil Analisis",
                        date = formattedDate, // Updated date format
                        iconRes = R.drawable.ic_analysis,
                        labaKotor = historyData.labaKotor ?: 0,
                        bayarGaji = historyData.bayarGaji ?: 0,
                        bayarAir = historyData.bayarAir ?: 0,
                        biayaListrik = historyData.biayaListrik ?: 0,
                        biayaTransport = historyData.biayaTransport ?: 0,
                        biayaPromosi = historyData.biayaPromosi ?: 0,
                        biayaPackaging = historyData.biayaPackaging ?: 0,
                        biayaPajak = historyData.biayaPajak ?: 0
                    )
                }.orEmpty()
                adapter.updateData(mappedItems)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
