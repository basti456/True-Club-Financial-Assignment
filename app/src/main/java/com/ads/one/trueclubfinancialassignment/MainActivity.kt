package com.ads.one.trueclubfinancialassignment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ads.one.trueclubfinancialassignment.adapters.DetailsAdapter
import com.ads.one.trueclubfinancialassignment.databinding.ActivityMainBinding
import com.ads.one.trueclubfinancialassignment.modals.DetailsModal
import com.ads.one.trueclubfinancialassignment.utils.CheckNetworkConnection
import com.ads.one.trueclubfinancialassignment.utils.MySingleton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var detailsList: ArrayList<DetailsModal>
    private lateinit var adapterDetail: DetailsAdapter
    private lateinit var checkNetworkConnection: CheckNetworkConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detailsList = ArrayList()
        binding.tryAgainButton.setOnClickListener {
            callNetworkConnection()
        }
        callNetworkConnection()
    }

    private fun callNetworkConnection() {
        checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this) { isConnected ->
            if (isConnected) {
                binding.clNoInternet.visibility = View.GONE
                binding.clInternet.visibility = View.VISIBLE
                val layoutManager = LinearLayoutManager(this)
                binding.recyclerDetails.layoutManager = layoutManager
                binding.recyclerDetails.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                    )
                )
                binding.searchButton.setOnClickListener {
                    binding.progressBar.visibility = View.VISIBLE
                    val name = binding.etName.text.toString()
                    sendRequest(name)
                }
                adapterDetail = DetailsAdapter(this, detailsList)
                binding.recyclerDetails.adapter = adapterDetail
            } else {
                binding.clInternet.visibility = View.GONE
                binding.clNoInternet.visibility = View.VISIBLE
            }
        }

    }

    private fun sendRequest(name: String) {
        try {
            val jsonObjectRequest = object :
                JsonObjectRequest(Request.Method.GET,
                    "https://api.nationalize.io/?name=$name",
                    null,
                    Response.Listener { response ->
                        Log.d("Request", response.toString())
                        detailsList.clear()
                        val result = response.getJSONArray("country")
                        for (i in 0 until result.length()) {
                            val res = result.getJSONObject(i)
                            val country = res.getString("country_id")
                            val probability = res.getDouble("probability")
                            Log.d("country", country)
                            Log.d("probability", probability.toString())
                            val details = DetailsModal(country, probability.toString())
                            detailsList.add(details)
                        }
                        binding.progressBar.visibility = View.GONE
                        if (detailsList.size == 0) {
                            binding.recyclerDetails.visibility = View.GONE
                            binding.noDataFound.visibility = View.VISIBLE
                        } else {
                            binding.noDataFound.visibility = View.GONE
                            binding.recyclerDetails.visibility = View.VISIBLE
                            adapterDetail.updateData(detailsList)
                        }
                    },
                    Response.ErrorListener {
                        Log.d("error aa raha", it.toString());
                    }) {

            }
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        } catch (_: Exception) {

        }
    }
}