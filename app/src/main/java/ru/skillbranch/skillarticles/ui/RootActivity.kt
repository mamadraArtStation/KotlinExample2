 package ru.skillbranch.skillarticles.ui

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_bottombar.*
import kotlinx.android.synthetic.main.layout_submenu.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.viewmodels.ArticleState
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import ru.skillbranch.skillarticles.viewmodels.Notify
import ru.skillbranch.skillarticles.viewmodels.ViewModelFactory


 class RootActivity : AppCompatActivity() {
     private lateinit var viewModel: ArticleViewModel

     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.menu_search, menu)
         val menuItem = menu?.findItem(R.id.action_search)

         menuItem?.expandActionView()
         viewModel.handleIsSearch(true)

         with(menuItem?.actionView as SearchView) {
             onActionViewExpanded()

             setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                 override fun onQueryTextSubmit(query: String?): Boolean {
                     viewModel.handleSearchQuery(query)
                     return false                 }

                 override fun onQueryTextChange(newText: String?): Boolean {
                     if(!newText.isNullOrBlank()) {
                         viewModel.handleSearchQuery(newText)
                     }
                     return false                 }

             })

             setOnQueryTextFocusChangeListener { v, hasFocus ->
                 if(!hasFocus) {
                     viewModel.handleIsSearch(false)
                 }
             }

             if(viewModel.getCurrentState()!!.isSearch) {
                 menuItem.expandActionView()
                 onActionViewExpanded()
                 setQuery(viewModel.getCurrentState()?.searchQuery, true)
             }
         }
         return super.onCreateOptionsMenu(menu)
     }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setUpToolbar()
        setupBottomBar()
        setupSubmenu()

        val vmFactory = ViewModelFactory("0")
        viewModel = ViewModelProviders.of(this, vmFactory).get(ArticleViewModel::class.java)
         viewModel.observeState(this) {
             renderUI(it)
         }

         viewModel.observeNotifications(this) {
             renderNotifications(it)
         }
    }

     private fun setUpToolbar() {
         setSupportActionBar(toolbar)
         supportActionBar?.setDisplayHomeAsUpEnabled(true)
         val logo = if(toolbar.childCount > 2) toolbar.getChildAt(2) as ImageView else null
         logo?.scaleType = ImageView.ScaleType.CENTER_CROP
         (logo?.layoutParams as Toolbar.LayoutParams).let {
             it.width = dpToIntPx(40)
             it.height = dpToIntPx(40)
             it.marginEnd = dpToIntPx(16)
             logo.layoutParams = it
         }
     }

     private fun renderUI(data: ArticleState) {
         btn_settings.isChecked = data.isShowMenu
         if(data.isShowMenu) submenu.open() else submenu.close()

         btn_like.isChecked = data.isLike
         btn_bookmark.isChecked = data.isBookmark

         switch_mode.isChecked = data.isDarkMode
         delegate.localNightMode = if(data.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
         if(data.isBigText) {
             tv_text_content.textSize = 18f
             btn_text_up.isChecked = true
             btn_text_down.isChecked = false
         } else {
             tv_text_content.textSize = 14f
             btn_text_up.isChecked = false
             btn_text_down.isChecked = true
         }

         tv_text_content.text = if (data.isLoadingContent) "loading" else data.content.first() as String

         toolbar.title = data.title ?: "Skill Articles"
         toolbar.subtitle = data.category ?: "loading..."

         if(data.categoryIcon != null) {

             val lo = getDrawable(data.categoryIcon as Int)
         }

         if(data.categoryIcon != null) toolbar.logo = this.resources.getDrawable(data.categoryIcon as Int)
     }

     private fun renderNotifications(notify: Notify) {
         val snackbar = Snackbar.make(coordinator_container, notify.message, Snackbar.LENGTH_LONG)

         when(notify) {
             is Notify.TextMessage -> {}
             is Notify.ActionMessage -> {
                 snackbar.setActionTextColor(this.resources.getColor(R.color.color_accent_dark))
                 snackbar.setAction(notify.actionLabel) {
                     notify.actionHandler?.invoke()
                 }
             }
             is Notify.ErrorMessage -> {
                 with(snackbar) {
                     setBackgroundTint(resources.getColor(R.color.design_default_color_error))
                     setTextColor(getColor(android.R.color.white))
                     setActionTextColor(resources.getColor(android.R.color.white))
                     setAction(notify.errLabel) {
                         notify.errHandler?.invoke()
                     }
                 }
             }
         }
         snackbar.show()
     }

     private fun setupBottomBar() {
         btn_like.setOnClickListener { viewModel.handleLike() }
         btn_bookmark.setOnClickListener { viewModel.handleBookmark() }
         btn_share.setOnClickListener { viewModel.handleShare() }
         btn_settings.setOnClickListener { viewModel.handleToggleMenu() }
     }

     private fun setupSubmenu() {
         btn_text_up.setOnClickListener { viewModel.handleUpText() }
         btn_text_down.setOnClickListener { viewModel.handleDownText() }
         switch_mode.setOnClickListener { viewModel.handleNightMode() }
     }
 }