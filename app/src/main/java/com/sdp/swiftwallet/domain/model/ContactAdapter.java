package com.sdp.swiftwallet.domain.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.databinding.ItemContainerContactBinding;

import java.util.List;

/**
 * UI adapter for contacts representation
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    private final List<Contact> contacts;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    /**
     * Initiate the contacts list view
     */
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerContactBinding itemContainerContactBinding = ItemContainerContactBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ContactViewHolder(itemContainerContactBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContactData(contacts.get(position));
    }

    /**
     * @return number of contacts
     */
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    /**
     * Represents a single contact inside the list adapter
     */
    class ContactViewHolder extends RecyclerView.ViewHolder {
        ItemContainerContactBinding binding;

        ContactViewHolder(ItemContainerContactBinding itemContainerContactBinding) {
            super(itemContainerContactBinding.getRoot());
            binding = itemContainerContactBinding;
        }

        /**
         * Sets basic information about the contacts
         * @param contact
         */
        void setContactData(Contact contact) {
            binding.contactNameTv.setText(contact.username);
            binding.contactEmailTv.setText(contact.email);
            binding.contactProfileImage.setImageBitmap(getContactImage(contact.image));
        }
    }

    /**
     * Returns the encoded image as BitMap
     */
    private Bitmap getContactImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
