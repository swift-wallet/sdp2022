package com.sdp.swiftwallet.domain.object.wallet.cryptography;

import com.sdp.swiftwallet.domain.mocks.MockWeb3Requests;
import com.sdp.swiftwallet.domain.model.object.transaction.TransactionHelper;
import com.sdp.swiftwallet.domain.repository.web3.IWeb3Requests;
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
