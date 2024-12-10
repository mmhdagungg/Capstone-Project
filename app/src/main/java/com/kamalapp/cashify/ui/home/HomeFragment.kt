package com.kamalapp.cashify.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.artikel.Artikel
import com.kamalapp.cashify.databinding.FragmentHomeBinding
import com.kamalapp.cashify.ui.artikel.ArtikelActivity
import com.kamalapp.cashify.ui.artikel.ListArtikelAdapter
import com.kamalapp.cashify.ui.profile.ProfileActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val artikelList = ArrayList<Artikel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Mengisi data artikel
        artikelList.addAll(getArtikelList())

        // Mengatur adapter RecyclerView
        val listArtikelAdapter = ListArtikelAdapter(artikelList)
        listArtikelAdapter.setOnItemClickCallback(object : ListArtikelAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Artikel) {
                Toast.makeText(requireContext(), "Kamu memilih ${data.judul}", Toast.LENGTH_SHORT).show()

                // Navigasi ke ArtikelActivity dengan data yang dipilih
                val intent = Intent(requireContext(), ArtikelActivity::class.java)
                intent.putExtra("URL", data.link)
                startActivity(intent)
            }
        })

        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext()) // Tambahkan ini
            adapter = listArtikelAdapter
            setHasFixedSize(true)
        }


        // Navigasi ke profil
        binding.ivProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun getArtikelList(): ArrayList<Artikel> {
        val judulArray = resources.getStringArray(R.array.judul_artikel)
        val deskripsiArray = resources.getStringArray(R.array.deskripsi_artikel)
        val gambarArray = resources.obtainTypedArray(R.array.gambar_artikel)
        val kategoriArray = resources.getStringArray(R.array.kategori_artikel)
        val linkArray = resources.getStringArray(R.array.link_artikel)

        val listArtikel = ArrayList<Artikel>()
        for (i in judulArray.indices) {
            val artikel = Artikel(
                judul = judulArray[i],
                deskripsi = deskripsiArray[i],
                gambar = gambarArray.getResourceId(i, -1),
                kategori = kategoriArray[i],
                link = linkArray[i]
            )
            listArtikel.add(artikel)
        }

        gambarArray.recycle()

        // Log untuk memastikan data terisi
        println("Artikel List: $listArtikel")
        return listArtikel
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
