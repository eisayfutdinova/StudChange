package com.example.user_vs.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.BitSet;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ApplyExchangeFragment extends Fragment {


    javax.mail.Session session=null;
    ProgressDialog progressDialog=null;
    Context context=null;
    String rec;
    String sub, text;

    public ApplyExchangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_exchange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        EditText apply_name, apply_country, apply_type, apply_cost, apply_link, apply_desc, apply_email;
        ImageView applyBtn;

        apply_name = view.findViewById(R.id.apply_name);
        apply_cost = view.findViewById(R.id.apply_costInformation);
        apply_country = view.findViewById(R.id.apply_county);
        apply_type = view.findViewById(R.id.apply_type);
        apply_link = view.findViewById(R.id.apply_link);
        apply_desc = view.findViewById(R.id.apply_description);
        apply_email = view.findViewById(R.id.apply_email);
        applyBtn = view.findViewById(R.id.apply_button);

        applyBtn.setOnClickListener(v->{
            final String apply_nameStr = apply_name.getText().toString();
            final String apply_costStr = apply_cost.getText().toString();
            final String apply_countryStr = apply_country.getText().toString();
            final String apply_typeStr = apply_type.getText().toString();
            final String apply_linkStr = apply_link.getText().toString();
            final String apply_descStr = apply_desc.getText().toString();
            final String apply_emailStr = apply_email.getText().toString();
            ModelOfApply modelOfApply = new ModelOfApply(apply_nameStr,apply_costStr,apply_countryStr,apply_typeStr,apply_linkStr,apply_descStr,apply_emailStr);

            if (apply_nameStr.isEmpty() || apply_costStr.isEmpty() || apply_countryStr.isEmpty() || apply_typeStr.isEmpty() || apply_linkStr.isEmpty() || apply_descStr.isEmpty() || apply_emailStr.isEmpty()) {
                showMessage("Please, verify all fields correctly");
            } else {
                db.collection("applies")
                        .document()
                        .set(modelOfApply);

                text = "Сообщение от " + apply_emailStr + "; Название программы: " + apply_nameStr + "; Страна стажировки: " + apply_countryStr + "; Тип стажировки: "+ apply_typeStr + "; Стоимость: " + apply_costStr + "; Описание: "+apply_descStr + "; Ссылка: "+ apply_linkStr;
                sendNew();
            }
        });
    }

    private void sendNew() {
        context=getContext();
        rec="lizasayfutdinova@gmail.com";
        sub="Test";

        Properties properties=new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session= Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("lizasayfutdinova@gmail.com", "marina1977");
            }
        });

        progressDialog=ProgressDialog.show(context, "", "Sending...", true);

        RetrieveFeedTask task=new RetrieveFeedTask();
        task.execute();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            try{

                Message message=new MimeMessage(session);
                message.setFrom(new InternetAddress("lizasayfutdinova@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(sub);
                message.setContent(text, "text/html; charset=utf-8");

                Transport.send(message);

            }catch (MessagingException e)
            {
                e.printStackTrace();
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

        }
    }

    private void showMessage(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

}

