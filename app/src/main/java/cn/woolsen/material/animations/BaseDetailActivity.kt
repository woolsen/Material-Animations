package cn.woolsen.material.animations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import cn.woolsen.material.animations.TransitionHelper.createSafeTransitionParticipants

open class BaseDetailActivity : AppCompatActivity() {
    fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun transitionTo(i: Intent?) {
        val pairs = createSafeTransitionParticipants(this, true)
        val transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
        startActivity(i, transitionActivityOptions.toBundle())
    }

    companion object {
        const val EXTRA_SAMPLE = "sample"
        const val EXTRA_TYPE = "type"
        const val TYPE_PROGRAMMATICALLY = 0
        const val TYPE_XML = 1
    }
}