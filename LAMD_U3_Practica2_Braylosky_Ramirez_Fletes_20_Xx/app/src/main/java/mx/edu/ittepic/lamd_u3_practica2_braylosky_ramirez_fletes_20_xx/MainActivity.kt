package mx.edu.ittepic.lamd_u3_practica2_braylosky_ramirez_fletes_20_xx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        agregar.setOnClickListener {
            insertarRegistro()
        }
        pedido.setOnClickListener {
            descripcion.isEnabled = pedido.isChecked
            precioProducto.isEnabled = pedido.isChecked
            cantidad.isEnabled = pedido.isChecked
            entregado.isEnabled = pedido.isChecked
        }

validar.setOnClickListener {
    validacion(celular.text.toString().toInt())
}

    }

private fun validacion(valor:Int) {
    baseRemota.collection("cliente")
        .whereEqualTo("celular", valor)
        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            var res = ""
            for (document in querySnapshot!!) {
                res += document.get("celular")
            }

            var com = valor.toString()

            if (res.equals(com)) {
                Toast.makeText(
                    this,
                    "ERROR, EL CLIENTE YA TIENE ASIGANADO UN PEDIDO",
                    Toast.LENGTH_LONG
                ).show()
                clearCampos()
            }else{
                Toast.makeText(this, "PULSA EL BOTON 'AGREGAR' PARA CAPTURAR EL PEDIDO", Toast.LENGTH_LONG).show()

            }
        }
}


    private fun insertarRegistro() {

                    var data = hashMapOf(
                        "nombre" to nombre.text.toString(),
                        "domicilio" to domicilio.text.toString(),
                        "celular" to celular.text.toString().toInt()
                    )

                    baseRemota.collection("cliente")
                        .add(data)
                        .addOnSuccessListener{


                            if(pedido.isChecked == true){
                                var producto = hashMapOf(
                                    "descripcion" to descripcion.text.toString(),
                                    "precio" to precioProducto.text.toString().toDouble(),
                                    "cantidad" to cantidad.text.toString().toInt(),
                                    "entregado" to entregado.text.toString().toBoolean()
                                )
                                baseRemota.collection("cliente")
                                    .document(it.id)
                                    .update("producto",producto as Map<String, Any>)

                            }
                            Toast.makeText(this,"SE CAPTURO CORRECTAMENTE EL PEDIDO", Toast.LENGTH_LONG).show()
                            clearCampos()
                            validar.isEnabled=true
                        }
                        .addOnFailureListener{
                            Toast.makeText(this,"ERROR, COMPRUEBE SU CONEXION A LA RED", Toast.LENGTH_LONG).show()
                        }
                }



    private fun clearCampos() {
        nombre.setText("")
        domicilio.setText("")
        celular.setText("")
        descripcion.setText("")
        precioProducto.setText("")
        cantidad.setText("")
        entregado.isChecked
    }



}
