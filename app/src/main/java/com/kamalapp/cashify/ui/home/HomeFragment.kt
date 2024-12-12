package com.kamalapp.cashify.ui.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.artikel.Artikel
import com.kamalapp.cashify.data.response.DashboardResponse
import com.kamalapp.cashify.data.response.ProfileResponse
import com.kamalapp.cashify.data.retrofit.ApiConfig
import com.kamalapp.cashify.databinding.FragmentHomeBinding
import com.kamalapp.cashify.ui.artikel.ArtikelActivity
import com.kamalapp.cashify.ui.artikel.ListArtikelAdapter
import com.kamalapp.cashify.ui.profile.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val artikelList = ArrayList<Artikel>()

    private var userName: String? = null
    private var cachedDashboardData: DashboardResponse? = null // Cache dashboard data

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupListeners()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        setLoadingPlaceholder()

        val sharedPref = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", 0)
        val token = sharedPref.getString("TOKEN", null)

        val currentMonthYearTV = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        val currentMonthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-yyyy"))
        binding.dateText.text = currentMonthYearTV

        if (!token.isNullOrEmpty() && userId != 0) {
            getUserProfileData(token)

            if (cachedDashboardData != null) {
                updateDashboardUI(cachedDashboardData!!)
            } else {
                loadDashboardData(token, userId, currentMonthYear)
            }
        } else {
            binding.tvMenyapa.text = getString(R.string.greeting_text, "User")
            Toast.makeText(requireContext(), "Data pengguna tidak valid, silakan login kembali.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLoadingPlaceholder() {
        _binding?.apply {
            tvRevenue.text = "..."
            tvExpenses.text = "..."
            tvNetBalance.text = "..."
            tvNetMargin.text = "..."
            tvHasilPrediksi.text = "Sedang Memuat Data"
        }
    }

    private fun getUserProfileData(token: String) {
        ApiConfig.instance.getUserProfile(token).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val profileData = response.body()
                    userName = profileData?.user?.name ?: "User"
                    binding.tvMenyapa.text = getString(R.string.greeting_text, userName)
                } else {
                    Log.e("ProfileData", "Error: ${response.message()}")
                    binding.tvMenyapa.text = getString(R.string.greeting_text, "User")
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("ProfileData", "Failure: ${t.message}")
                binding.tvMenyapa.text = getString(R.string.greeting_text, "User")
            }
        })
    }

    private fun setupRecyclerView() {
        artikelList.addAll(getArtikelList())

        val listArtikelAdapter = ListArtikelAdapter(artikelList)
        listArtikelAdapter.setOnItemClickCallback(object : ListArtikelAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Artikel) {
                val intent = Intent(requireContext(), ArtikelActivity::class.java).apply {
                    putExtra("URL", data.link)
                }
                startActivity(intent)
            }
        })

        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listArtikelAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        binding.ivProfile.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }
    }

    private fun loadDashboardData(token: String, userId: Int, dateTime: String) {
        ApiConfig.instance.getDashboardData(token, userId, dateTime)
            .enqueue(object : Callback<DashboardResponse> {
                override fun onResponse(
                    call: Call<DashboardResponse>,
                    response: Response<DashboardResponse>
                ) {
                    if (!isAdded || _binding == null) return

                    if (response.isSuccessful) {
                        response.body()?.let { dashboardData ->
                            cachedDashboardData = dashboardData // Cache data
                            updateDashboardUI(dashboardData)
                        } ?: setDefaultDashboardValues()
                    } else {
                        Log.e("DashboardData", "Error: ${response.message()}")
                        setDefaultDashboardValues()
                    }
                }

                override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                    Log.e("DashboardData", "Failure: ${t.message}")
                    setDefaultDashboardValues()
                }
            })
    }
    private fun updateDashboardUI(data: DashboardResponse) {
        _binding?.apply {
            val revenue = data.revenue ?: 0
            val expenses = data.expenses ?: 0

            // Menghitung netMargin
            val netMargin = if (revenue > 0) {
                ((revenue - expenses).toDouble() / revenue * 100)
            } else {
                0.0 // Menghindari pembagian dengan nol
            }

            tvRevenue.text = formatCurrency(revenue)
            tvExpenses.text = formatCurrency(expenses)
            tvNetBalance.text = formatCurrency(data.netBalance ?: 0)
            tvNetMargin.text = formatPercentage(netMargin)

            // Menampilkan hasil prediksi dan artikel berdasarkan nilai netMargin
            tvHasilPrediksi.text = getPredictionTextBasedOnNetMargin(netMargin)
            displayArticlesBasedOnNetMargin(netMargin)
        }
    }

    private fun getPredictionTextBasedOnNetMargin(netMargin: Double): String {
        return when {
            netMargin == 0.0 -> {
                getString(R.string.kosong)
            }
            netMargin < 10.0 -> {
                getString(R.string.prediction_low_margin)
            }
            netMargin in 10.0..15.0 -> {
                getString(R.string.prediction_mid_margin)
            }
            netMargin in 15.0..20.0 -> {
                getString(R.string.prediction_good_margin)
            }
            netMargin in 20.0..30.0 -> {
                getString(R.string.prediction_stable_margin)
            }
            netMargin > 30 -> {
                getString(R.string.prediction_high_margin)
            }
            else -> {
                getString(R.string.prediction_no_data)
            }
        }
    }



    private fun displayArticlesBasedOnNetMargin(netMargin: Double) {
        val filteredArticles = when {
            netMargin > 30 -> {
                artikelList.filter { it.kategori == "Keuangan Sehat" }
            }
            netMargin == 0.0 -> {
                artikelList.filter { it.kategori == "Masih Kosong"}
            }
            else -> {
                artikelList.filter { it.kategori == "Keuangan Kurang Sehat" }
            }
        }

        val listArtikelAdapter = ListArtikelAdapter(ArrayList(filteredArticles))
        listArtikelAdapter.setOnItemClickCallback(object : ListArtikelAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Artikel) {
                val intent = Intent(requireContext(), ArtikelActivity::class.java).apply {
                    putExtra("URL", data.link)
                }
                startActivity(intent)
            }
        })

        binding.rvArticles.adapter = listArtikelAdapter
    }




    private fun setDefaultDashboardValues() {
        _binding?.apply {
            tvRevenue.text = formatCurrency(0)
            tvExpenses.text = formatCurrency(0)
            tvNetBalance.text = formatCurrency(0)
            tvNetMargin.text = formatPercentage(0.0)
            tvHasilPrediksi.text = "Data tidak tersedia"
        }
    }

    private fun formatCurrency(value: Int): String {
        return String.format("Rp %,d", value).replace(",", ".")
    }

    private fun formatPercentage(value: Double): String {
        return String.format("%.1f%%", value) // Format dengan 1 angka di belakang koma
    }


    private fun getArtikelList(): ArrayList<Artikel> {
        val judulArray = resources.getStringArray(R.array.judul_artikel)
        val deskripsiArray = resources.getStringArray(R.array.deskripsi_artikel)
        val gambarArray = resources.obtainTypedArray(R.array.gambar_artikel)
        val kategoriArray = resources.getStringArray(R.array.kategori_artikel)
        val linkArray = resources.getStringArray(R.array.link_artikel)

        val listArtikel = ArrayList<Artikel>()
        for (i in judulArray.indices) {
            listArtikel.add(
                Artikel(
                    judul = judulArray[i],
                    deskripsi = deskripsiArray[i],
                    gambar = gambarArray.getResourceId(i, -1),
                    kategori = kategoriArray[i],
                    link = linkArray[i]
                )
            )
        }
        gambarArray.recycle()
        return listArtikel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}