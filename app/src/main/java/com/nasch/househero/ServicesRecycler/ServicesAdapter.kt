import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nasch.househero.R

class ServicesAdapter : RecyclerView.Adapter<ServicesViewHolder>() {
    private var listaResultados: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_services, parent, false)
        return ServicesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val resultado = listaResultados[position]
        holder.bind(resultado)
    }

    override fun getItemCount(): Int {
        return listaResultados.size
    }

    fun actualizarResultados(nuevaLista: List<String>) {
        listaResultados = nuevaLista
        notifyDataSetChanged()
    }
}
