package com.sdp.swiftwallet.presentation.wallet;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.view.ViewGroup;

import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemRecyclerViewAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

@RunWith(JUnit4.class)
public class WalletItemRecyclerTest {
    public static String randomPK = "0x0009876898789";

    static ArrayList<WalletItemRecyclerViewAdapter.WalletItem> values;
    static {
        values = new ArrayList<>();
        values.add(new WalletItemRecyclerViewAdapter.WalletItem(randomPK));
        values.add(new WalletItemRecyclerViewAdapter.WalletItem(randomPK));
    }

    @Test
    public void shouldBeAbleToCreateARecycler(){
        WalletItemRecyclerViewAdapter walletItemRecyclerViewAdapter = new WalletItemRecyclerViewAdapter(values);
    }

    @Test
    public void shouldBeAbleToGetItemCount() {
        WalletItemRecyclerViewAdapter walletItemRecyclerViewAdapter = new WalletItemRecyclerViewAdapter(values);
        assertEquals(walletItemRecyclerViewAdapter.getItemCount(), values.size());
    }
}
