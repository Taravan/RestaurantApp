package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import com.isp.restaurantapp.R
import com.isp.restaurantapp.adapters.MenuAdapter
import com.isp.restaurantapp.adapters.MenuHolderAdapter
import com.isp.restaurantapp.repositories.dataMock
import com.isp.restaurantapp.viewModels.MenuVM
import com.isp.restaurantapp.viewModels.MenuVMFactory

class MenuHolderFragment : Fragment() {

    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        TODO()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}