package com.sdp.swiftwallet.presentation.ItemsFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.wallet.WalletKeyPair;
import com.sdp.swiftwallet.presentation.friend.fragments.FriendItem;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItem;
import com.sdp.swiftwallet.presentation.wallet.fragments.WalletItemRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of wallet items.
 */
public abstract class ItemFragment extends Fragment {
    protected final int list;

    public ItemFragment(int list) {
        this.list = list;
    }
}