package com.example.shopnow.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shopnow.R
import com.example.shopnow.adapter.ProductRecyclerViewAdapter
import com.example.shopnow.data_class.Product

class AllProductFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_product, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.all_product_list)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(false)

        val products = listOf(
            Product("Product 1", 10.99, "https://akidoo.top/images/covers/eroriman-2-cv1.png", "4.5", "Lorem ipsum dolor sit amet.", "Seller 1", "https://akidoo.top/images/covers/eroriman-2-cv1.png", "Food"),
            Product("Product 2", 19.99, "https://git-covers.pages.dev/images/daraku-reijou-the-animation-1.jpg", "4.2", "Consectetur adipiscing elit.", "Seller 2", "https://git-covers.pages.dev/images/daraku-reijou-the-animation-1.jpg", "Food"),
            Product("Product 3", 8.49, "https://cdn.statically.io/img/akidoo.top/f=auto,q=100/images/covers/doukyuusei-remake-1-cv1.png", "3.9", "Nulla nec feugiat ante.", "Seller 3", "https://cdn.statically.io/img/akidoo.top/f=auto,q=100/images/covers/doukyuusei-remake-1-cv1.png", "Food"),
            Product("Product 4", 14.95, "https://ba.alphafish.top/images/covers/eroge-de-subete-wa-kaiketsu-dekiru-1-cv1.png", "4.7", "Sed vel arcu eget arcu.", "Seller 4", "https://ba.alphafish.top/images/covers/eroge-de-subete-wa-kaiketsu-dekiru-1-cv1.png", "Technology"),
            Product("Product 5", 5.99, "https://akidoo.top/images/covers/eroriman-1-cv1.png", "4.0", "Fusce nec fermentum purus.", "Seller 5", "https://akidoo.top/images/covers/eroriman-1-cv1.png", "Technology"),
            Product("Product 6", 12.50, "https://git-covers.pages.dev/images/gakuen-de-jikan-yo-tomare-1.jpg", "4.3", "Vestibulum tincidunt auctor nunc.", "Seller 6", "https://git-covers.pages.dev/images/gakuen-de-jikan-yo-tomare-1.jpg", "Technology"),
            Product("Product 7", 9.99, "https://git-covers.pages.dev/images/gakuen-de-jikan-yo-tomare-2.jpg", "4.1", "Morbi sed est lobortis.", "Seller 7", "https://git-covers.pages.dev/images/gakuen-de-jikan-yo-tomare-2.jpg", "Daily"),
            Product("Product 8", 17.99, "https://git-covers.pages.dev/images/gakuen-de-jikan-yo-tomare-3.jpg", "4.6", "Donec consequat mi ut nisl.", "Seller 8", "https://git-covers.pages.dev/images/gakuen-de-jikan-yo-tomare-3.jpg", "Daily"),
            Product("Product 9", 6.75, "https://git-covers.pages.dev/images/gakuen-de-jikan-yo-tomare-4.jpg", "3.8", "Pellentesque ac arcu cursus.", "Seller 9", "https://git-covers.pages.dev/images/gakuen-de-jikan-yo-tomare-4.jpg", "Daily"),
            Product("Product 10", 11.49, "https://cdn.statically.io/img/akidoo.top/f=auto,q=100/images/covers/sei-dorei-gakuen-2-ep-2-cv1.png", "4.4", "Quisque eget sem aliquam.", "Seller 10", "https://cdn.statically.io/img/akidoo.top/f=auto,q=100/images/covers/sei-dorei-gakuen-2-ep-2-cv1.png", "Daily")
        )

        recyclerView.adapter = ProductRecyclerViewAdapter(products, view.context)

        return view
    }
}