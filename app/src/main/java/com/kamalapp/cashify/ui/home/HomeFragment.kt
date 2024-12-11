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

        val currentMonthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-yyyy"))
        binding.dateText.text = currentMonthYear

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
            tvHasilPrediksi.text = "..."
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
            tvRevenue.text = formatCurrency(data.revenue ?: 0)
            tvExpenses.text = formatCurrency(data.expenses ?: 0)
            tvNetBalance.text = formatCurrency(data.netBalance ?: 0)
            tvNetMargin.text = formatPercentage(data.netMargin ?: 0)
            tvHasilPrediksi.text = data.hasilPrediksi ?: "Tidak ada prediksi"
        }
    }

    private fun setDefaultDashboardValues() {
        _binding?.apply {
            tvRevenue.text = formatCurrency(0)
            tvExpenses.text = formatCurrency(0)
            tvNetBalance.text = formatCurrency(0)
            tvNetMargin.text = formatPercentage(0)
            tvHasilPrediksi.text = "Data tidak tersedia"
        }
    }

    private fun formatCurrency(value: Int): String {
        return String.format("Rp %,d", value).replace(",", ".")
    }

    private fun formatPercentage(value: Int): String {
        return "$value%"
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