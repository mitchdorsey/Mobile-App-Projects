/**
 * IMPORTANT: Make sure you are using the correct package name. 
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.coffeepurchase;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int quantity = 2;
    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrderEmail(View view) {

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        EditText nameEntered = (EditText) findViewById(R.id.name_field);
        String customerName = nameEntered.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);

        String orderMessage = createOrderSummary(customerName, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_TEXT, orderMessage);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + customerName);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void submitOrderText(View view) {

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        EditText nameEntered = (EditText) findViewById(R.id.name_field);
        String customerName = nameEntered.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);

        String orderMessage = createOrderSummary(customerName, price, hasWhippedCream, hasChocolate);


            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
            intent.putExtra("sms_body", orderMessage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

    }

    /**
     * Creates message for the conditions of the order
     * @param name of the customer
     * @param price of the order
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate  is whether or not the user wants chocolate topping
     * @return text summary
     * */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate){

        String priceMessage = "Name: " + name;
        priceMessage += "\nAdd Whipped cream? " + addWhippedCream;
        priceMessage += "\nAdd Chocolate? " + addChocolate;
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal: $" + price;
        priceMessage += "\nThank You!";


        return priceMessage;
    }

    /**
     * This method is called when the plus [+] button is clicked.
     */
    public void increment(View view){

        if(quantity == 100){
            return;
        }
        quantity++;
        display(quantity);
    }

    /**
     * This method is called when the minus [-] button is clicked.
     */
    public void decrement(View view){

        if(quantity == 1)
        {
            return;
        }
        quantity--;
        display(quantity);
    }

    /**
     * Calculates the price of the order
     * @return price of the order
     * */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate){

        int basePrice = 5;

        if(hasWhippedCream){
            basePrice += 1;
        }
        if(hasChocolate){
            basePrice += 2;
        }

        return basePrice * quantity;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}