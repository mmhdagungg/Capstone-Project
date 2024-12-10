package com.kamalapp.cashify.ui.history

import HistoryAdapter
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

        val adapter = HistoryAdapter(getHistoryData()) {
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java)
            startActivity(intent)
        }
        binding.rvHistory.adapter = adapter

        binding.edTanggal.setOnClickListener {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Data dummy untuk diisi ke RecyclerView
    private fun getHistoryData(): List<HistoryItem> {
        return listOf(
            HistoryItem("Hasil Analisis", "11-11-2024", R.drawable.ic_analysis),
            HistoryItem("Hasil Analisis", "12-11-2024", R.drawable.ic_analysis),
            HistoryItem("Hasil Analisis", "13-11-2024", R.drawable.ic_analysis),
            HistoryItem("Hasil Analisis", "14-11-2024", R.drawable.ic_analysis)
        )
    }
}
