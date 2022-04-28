package com.sdp.swiftwallet.JavaTest.wallet.cryptography;

import com.sdp.swiftwallet.JavaTest.mocks.MockWeb3Requests;
import com.sdp.swiftwallet.domain.model.wallet.TransactionHelper;
import com.sdp.swiftwallet.domain.repository.IWeb3Requests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;

@RunWith(JUnit4.class)
public class TransactionHelperTest {
    @Test
    public void shouldBeAbleToCreateATransaction(){
        IWeb3Requests mockWeb3 = new MockWeb3Requests();
        TransactionHelper.createTransaction(mockWeb3, "From", "To", BigInteger.ONE);
    }
}
