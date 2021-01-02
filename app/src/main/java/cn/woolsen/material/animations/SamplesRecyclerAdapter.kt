package cn.woolsen.material.animations

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.woolsen.material.animations.SamplesRecyclerAdapter.SamplesViewHolder
import cn.woolsen.material.animations.TransitionHelper.createSafeTransitionParticipants
import cn.woolsen.material.animations.databinding.RowSampleBinding

class SamplesRecyclerAdapter(private val activity: Activity, private val samples: List<Sample>) : RecyclerView.Adapter<SamplesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SamplesViewHolder {
        val binding = RowSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SamplesViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: SamplesViewHolder, position: Int) {
        val sample = samples[viewHolder.adapterPosition]
        viewHolder.binding.sample = sample
        viewHolder.binding.sampleLayout.setOnClickListener {
            when (viewHolder.adapterPosition) {
                0 -> transitionToActivity(TransitionActivity1::class.java, sample)
                1 -> transitionToActivity(SharedElementActivity::class.java, viewHolder, sample)
                2 -> transitionToActivity(AnimationsActivity1::class.java, sample)
                3 -> transitionToActivity(RevealActivity::class.java, viewHolder, sample, R.string.transition_reveal1)
            }
        }
    }

    private fun transitionToActivity(target: Class<*>, sample: Sample) {
        val pairs = createSafeTransitionParticipants(activity, true)
        startActivity(target, pairs, sample)
    }

    private fun transitionToActivity(target: Class<*>, viewHolder: SamplesViewHolder, sample: Sample, transitionName: Int) {
        val pairs = createSafeTransitionParticipants(activity, false,
                Pair(viewHolder.binding.sampleIcon, activity.getString(transitionName)))
        startActivity(target, pairs, sample)
    }

    private fun transitionToActivity(target: Class<*>, viewHolder: SamplesViewHolder, sample: Sample) {
        val pairs = createSafeTransitionParticipants(activity, false,
                Pair(viewHolder.binding.sampleIcon, activity.getString(R.string.square_blue_name)),
                Pair(viewHolder.binding.sampleName, activity.getString(R.string.sample_blue_title)))
        startActivity(target, pairs, sample)
    }

    private fun startActivity(target: Class<*>, pairs: Array<Pair<View, String>>, sample: Sample) {
        val i = Intent(activity, target)
        val transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs)
        i.putExtra("sample", sample)
        activity.startActivity(i, transitionActivityOptions.toBundle())
    }

    override fun getItemCount(): Int {
        return samples.size
    }

    inner class SamplesViewHolder(rootView: View) : ViewHolder(rootView) {
        val binding = DataBindingUtil.bind<RowSampleBinding>(rootView)!!
    }
}