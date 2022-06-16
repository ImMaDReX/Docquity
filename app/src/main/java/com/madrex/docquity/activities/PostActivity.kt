package com.madrex.docquity.activities

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.madrex.docquity.viewModels.MainViewModel
import com.madrex.docquity.R
import com.madrex.docquity.databinding.ActivityMainBinding
import com.madrex.docquity.databinding.PostDetailBinding
import com.madrex.docquity.models.Post
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var postAdapter: PostAdapter
    var disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.postsLD.observe(this, Observer {
            if(it?.size!! >0){
                postAdapter.submitList(it)
                binding.noPostFound.visibility = View.GONE
                binding.posts.visibility = View.VISIBLE
            } else {
                binding.noPostFound.visibility = View.VISIBLE
                binding.posts.visibility = View.GONE
            }
        })
        postAdapter = PostAdapter(this, mutableListOf())
        binding.posts.adapter = postAdapter

    }

    class PostAdapter(private val context: Activity, private var items: List<Post>)
        : ArrayAdapter<Post>(context, 0, items) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val binding = PostDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.post = items[position]
            return binding.root
        }
        override fun getCount(): Int {
            return items.size
        }


        fun submitList(items: List<Post>) {
            this.items = items
            notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView? = searchItem?.actionView as SearchView
        val searchObserve = Observable
            .create<String> { emitter ->
                searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        if (newText.isNotEmpty() && !emitter.isDisposed)
                            emitter.onNext(newText)
                        else
                            mainViewModel.getPosts()
                        return false
                    }
                })
            }.debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        searchObserve.subscribe(object : io.reactivex.Observer<String?> {
            override fun onSubscribe(d: Disposable) {
                disposables.add(d)
            }

            override fun onNext(t: String) {
               mainViewModel.getPost(t)
            }

            override fun onError(e: Throwable) {}

            override fun onComplete() {}

        })
        return super.onCreateOptionsMenu(menu)
    }
}