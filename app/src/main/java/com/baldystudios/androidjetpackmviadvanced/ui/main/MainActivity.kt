package com.baldystudios.androidjetpackmviadvanced.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.ui.BaseActivity
import com.baldystudios.androidjetpackmviadvanced.ui.auth.AuthActivity
import com.baldystudios.androidjetpackmviadvanced.ui.main.account.ChangePasswordFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.account.UpdateAccountFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.UpdateBlogFragment
import com.baldystudios.androidjetpackmviadvanced.ui.main.blog.ViewBlogFragment
import com.baldystudios.androidjetpackmviadvanced.util.BottomNavController
import com.baldystudios.androidjetpackmviadvanced.util.BottomNavController.*
import com.baldystudios.androidjetpackmviadvanced.util.setUpNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),
    NavGraphProvider,
    OnNavigationGraphChanged,
    OnNavigationReselectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.nav_blog,
            this,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionbar()

        bottomNavigationView = bottom_navigation_view
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }

        subscribeObservers()
    }

    private fun setupActionbar() {
        setSupportActionBar(tool_bar)
    }

    fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, Observer { authToken ->

            Log.d(TAG, "MainActivity: subscribeObservers: AuthToken $authToken")

            if (authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                navAuthActivity()
                finish()
            }

        })
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayProgressBar(bool: Boolean) {
        progress_bar.visibility = if (bool) VISIBLE else GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item!!)
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun getNavGraphId(itemId: Int) = when (itemId) {

        R.id.nav_blog -> {
            R.navigation.nav_blog
        }

        R.id.nav_account -> {
            R.navigation.nav_account
        }

        R.id.nav_create_blog -> {
            R.navigation.nav_create_blog
        }

        else -> {
            R.navigation.nav_blog
        }

    }


    override fun onGraphChange() {

    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) =
        when (fragment) {
            is ViewBlogFragment -> {
                navController.navigate(R.id.action_viewBlogFragment_to_home)
            }

            is UpdateBlogFragment -> {
                navController.navigate(R.id.action_updateBlogFragment_to_home)
            }

            is UpdateAccountFragment -> {
                navController.navigate(R.id.action_updateAccountFragment_to_home)
            }

            is ChangePasswordFragment -> {
                navController.navigate(R.id.action_changePasswordFragment_to_home)
            }

            else -> {
                // do nothing
            }
        }

}
