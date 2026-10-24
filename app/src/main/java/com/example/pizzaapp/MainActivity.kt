package com.example.pizzaapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizzaapp.ui.theme.PizzaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PizzaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PizzaOrderScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PizzaOrderScreen(
    modifier: Modifier = Modifier,
    viewModel: PizzaViewModel = viewModel()
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Pizza Order", style = MaterialTheme.typography.headlineSmall)
        HorizontalDivider()

        OutlinedTextField(
            value = viewModel.customerName,
            onValueChange = viewModel::onNameChange,
            label = { Text("Customer name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Choose pizza size")
        viewModel.sizes.forEach { size ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = viewModel.selectedSize == size,
                    onClick = { viewModel.onSizeSelected(size) }
                )
                Text(size.name, modifier = Modifier.weight(1f))
                Text(formatPrice(size.price))
            }
        }
        HorizontalDivider()

        Text("Extra toppings (+${formatPrice(viewModel.toppingPrice)} each)")
        viewModel.toppings.forEach { topping ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = topping in viewModel.selectedToppings,
                    onCheckedChange = { viewModel.onToppingChecked(topping, it) }
                )
                Text(topping)
            }
        }
        HorizontalDivider()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Home delivery")
                Text(
                    "Delivery fee: ${formatPrice(viewModel.deliveryFee)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(
                checked = viewModel.homeDelivery,
                onCheckedChange = viewModel::onDeliveryChange
            )
        }
        HorizontalDivider()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Total price", modifier = Modifier.weight(1f))
            Text(
                formatPrice(viewModel.totalPrice),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Order placed for ${viewModel.customerName}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            enabled = viewModel.canPlaceOrder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PizzaOrderScreenPreview() {
    PizzaAppTheme {
        PizzaOrderScreen()
    }
}
