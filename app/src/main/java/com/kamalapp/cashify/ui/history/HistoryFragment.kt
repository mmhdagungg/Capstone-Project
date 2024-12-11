package com.kamalapp.cashify.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamalapp.cashify.MyApplication
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import java.util.Calendar
import java.text.SimpleDateFormat
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.HistoryItem
import com.kamalapp.cashify.databinding.FragmentHistoryBinding
import com.kamalapp.cashify.ui.detailHistory.DetailHistoryActivity
import androidx.lifecycle.Observer
import com.kamalapp.cashify.data.response.Data
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
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java)
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
                    calendar.set(year, month, 1)
                    val newFormattedDate = dateFormat.format(calendar.time)
                    binding.edTanggal.setText(
                        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
                    ) // Format dengan nama bulan
                    loadHistoryData(adapter, token, userId, newFormattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.findViewById<View>(
                    resources.getIdentifier("android:id/day", null, null)
                )?.visibility = View.GONE
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
            viewModel.getHistory(it, userId, dateTime ?: "").observe(viewLifecycleOwner, Observer { response ->
                if (response != null && response.data != null) {
                    val mappedItems = response.data.map { historyData ->
                        HistoryItem(
                            title = historyData.labaKotor?.toString() ?: "No data",
                            date = historyData.dateAdded ?: "Unknown Date",
                            iconRes = R.drawable.ic_analysis
                        )
                    }
                    adapter.updateData(mappedItems)
                } else {
                    Log.e("HistoryFragment", "Response data is null or empty")
                    adapter.updateData(emptyList())
                }
            })
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
