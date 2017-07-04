package com.cielo.payment_lio_poc;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.cielo.payment_lio_poc.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cielo.orders.domain.Credentials;
import cielo.orders.domain.Order;
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.order.payment.PaymentError;
import cielo.sdk.order.payment.PaymentListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends BaseActivity {

    Button chocolateBT;
    Button morangoBT;
    Button acaiBT;
    Button amendoimBT;
    Button tapiocaBT;
    Button cocoBT;

    Bitmap thumbnail;
    File pic;

    protected static final int CAMERA_PIC_REQUEST = 1;
    int valor;
    String id;

    private OrderManager orderManager;
    private Order order;

    public void onCreate(Bundle savedInstanceState) {
        order();
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.primeiratela);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        chocolateBT = (Button) findViewById(R.id.chocolateBTid);
        morangoBT = (Button) findViewById(R.id.morangoBTid);
        acaiBT = (Button) findViewById(R.id.acaiBTid);
        amendoimBT = (Button) findViewById(R.id.amendoimBTid);
        tapiocaBT = (Button) findViewById(R.id.tapiocaBTid);
        cocoBT = (Button) findViewById(R.id.cocoBTid);

        chocolateBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                valor = 3000;
                Intent cameraIntent1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent1, CAMERA_PIC_REQUEST);
            }
        });
        morangoBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                valor = 10000;
                Intent cameraIntent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent2, CAMERA_PIC_REQUEST);
            }
        });
        acaiBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                valor = 1000;
                Intent cameraIntent3 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent3, CAMERA_PIC_REQUEST);
            }
        });
        amendoimBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                valor = 500;
                Intent cameraIntent3 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent3, CAMERA_PIC_REQUEST);
            }
        });
        tapiocaBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                valor = 2000;
                Intent cameraIntent5 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent5, CAMERA_PIC_REQUEST);
            }
        });
        cocoBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                valor = 5000;
                Intent cameraIntent6 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent6, CAMERA_PIC_REQUEST);
            }
        });
    }

    private void order() {
        Credentials credentials = new Credentials("yxyDxmlXXHvK", "vXlqw6BB8OEm");
        orderManager = new OrderManager(credentials, this);

        ServiceBindListener serviceBindListener = new ServiceBindListener() {
            @Override
            public void onServiceBound() {
                order = orderManager.createDraftOrder("Doação Galera do Bem");
                id=order.getId();
                String sku = "0000000000000001";
                String name = "Doação";

                int quantity = 1;
                int unitPrice = valor;
                String unityOfMeasure = "UNIDADE";

                order.addItem(sku, name, unitPrice, quantity, unityOfMeasure);

                orderManager.placeOrder(order);
            }

            @Override
            public void onServiceUnbound() {
                // O serviço foi desvinculado
            }
        };

        orderManager.bind(this, serviceBindListener);
    }

    public void orderRequest() {
        PaymentListener paymentListener = new PaymentListener() {
            @Override
            public void onStart() {
                Log.d("MinhaApp", "O pagamento começou.");
            }

            @Override
            public void onPayment(@NotNull Order order) {
                Log.d("MinhaApp", "Um pagamento foi realizado.");
            }

            @Override
            public void onCancel() {
                Log.d("MinhaApp", "A operação foi cancelada.");
            }

            @Override
            public void onError(@NotNull PaymentError paymentError) {
                Log.d("MinhaApp", "Houve um erro no pagamento.");
            }
        };
        orderManager.checkoutOrder(id, valor, paymentListener);

    }

    public void onBackPressed()  {}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode != RESULT_CANCELED) {
                order();
                orderRequest();
            }
            else{
                Intent irTela = new Intent(MainActivity.this, MainActivity.class);
                startActivity(irTela);
            }
        }
    }
}
