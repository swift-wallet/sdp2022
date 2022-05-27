package com.sdp.swiftwallet.presentation.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.currency.CurrencyBank;
import com.sdp.swiftwallet.domain.model.currency.SwiftWalletCurrencyBank;
import com.sdp.swiftwallet.domain.repository.firebase.SwiftAuthenticator;
import com.sdp.swiftwallet.presentation.cryptoGraph.CryptoValuesActivity;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import com.sdp.swiftwallet.domain.model.transaction.Transaction;
import com.sdp.swiftwallet.presentation.main.MainActivity;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class StatsFragment extends Fragment {

    private static final String COLLECTION_NAME = "transactions2";

    private static final String AMOUNT_KEY = "amount";
    private static final String CURRENCY_KEY = "currency";

    private static final String TRANSACTION_ID_KEY = "id";
    private static final String DATE_KEY = "date";

    private static final String SENDER_WALLET_KEY = "senderWallet";
    private static final String RECEIVER_WALLET_KEY = "receiverWallet";

    private static final String SENDER_ID_KEY = "senderID";
    private static final String RECEIVER_ID_KEY = "receiverID";

    @Inject
    CurrencyBank bank;
    @Inject
    SwiftAuthenticator authenticator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    /**
     * Sets up listeners on start
     */
    @Override
    public void onStart() {
        super.onStart();
        //This is temporary
        getActivity().findViewById(R.id.cryptovalues_button)
            .setOnClickListener((v) -> startCryptovalues());
        getActivity().findViewById(R.id.transaction_history_button)
            .setOnClickListener((v) -> startTransactionHistory());
        getActivity().findViewById(R.id.create_transaction_button)
            .setOnClickListener((v) -> createDummyTransaction());
    }

    // Buttons for starting appropriate activities

    private void startCryptovalues() {
        Intent intent = new Intent(getContext(), CryptoValuesActivity.class);
        startActivity(intent);
    }

    private void startTransactionHistory() {
        Intent intent = new Intent(getContext(), TransactionActivity.class);
        startActivity(intent);
    }

    /**
     * Create a dummy transaction, sets it on the firestor
     * to be changed later with real date
     */
    private void createDummyTransaction() {
        ((MainActivity) getActivity()).getIdlingResource().increment();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Random rand = new Random();
        Transaction.Builder builder = new Transaction.Builder();

        double amount = rand.nextDouble() * 200;
        Currency c = new Currency("bitcoin", "BTC", 10);
        builder.setAmountAndCurrency(amount, c);

        int id = rand.nextInt(1000);
        Date date = new Date();
        builder.setMetadata(id, date);

        String senderWallet = "SENDER_WALLET";
        String receiverWallet = "RECEIVER_WALLET";
        builder.setWalletIDs(senderWallet, receiverWallet);
        if (authenticator.getUid().isPresent()){
            if (rand.nextInt() % 2 == 0) {
                builder.setReceiverID(authenticator.getUid().get());
                if (rand.nextInt() % 2 == 0) {
                    builder.setSenderID("dumbo");
                }
            } else {
                builder.setSenderID(authenticator.getUid().get());
                if (rand.nextInt() % 2 == 0) {
                    builder.setReceiverID("dumbo");
                }
            }
        }

        Transaction t = builder.build();

        Map<String, Object> data = new HashMap<>();
        data.put(AMOUNT_KEY, t.getAmount());
        data.put(CURRENCY_KEY, t.getCurr());

        data.put(TRANSACTION_ID_KEY, t.getTransactionID());
        data.put(DATE_KEY, new Timestamp(t.getDate()));

        data.put(SENDER_WALLET_KEY, t.getSenderWalletID());
        data.put(RECEIVER_WALLET_KEY, t.getReceiverWalletID());

        if (t.getSenderID().isPresent()) {
            data.put(SENDER_ID_KEY, t.getSenderID().get());
        } else {
            data.put(SENDER_ID_KEY, "");
        }
        if (t.getReceiverID().isPresent()) {
            data.put(RECEIVER_ID_KEY, t.getReceiverID().get());
        } else {
            data.put(RECEIVER_ID_KEY, "");
        }

        db.collection(COLLECTION_NAME).document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ((MainActivity) getActivity()).getIdlingResource().decrement();
                    Toast.makeText(getActivity().getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}