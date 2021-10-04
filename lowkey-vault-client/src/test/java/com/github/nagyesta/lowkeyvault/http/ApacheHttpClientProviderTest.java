package com.github.nagyesta.lowkeyvault.http;

import com.azure.security.keyvault.keys.KeyAsyncClient;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyAsyncClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApacheHttpClientProviderTest {

    private static final String HTTPS_LOCALHOST_8443 = "https://localhost:8443";
    private static final String WEB_KEY_ID = HTTPS_LOCALHOST_8443 + "/keys/test/00000000000000000000000000000001";
    private final ApacheHttpClientProvider underTest = new ApacheHttpClientProvider(HTTPS_LOCALHOST_8443);

    @Test
    void testGetKeyAsyncClientShouldReturnClientWhenCalled() {
        //given

        //when
        final KeyAsyncClient client = underTest.getKeyAsyncClient();

        //then
        Assertions.assertNotNull(client);
    }

    @Test
    void testGetKeyClientShouldReturnClientWhenCalled() {
        //given

        //when
        final KeyClient client = underTest.getKeyClient();

        //then
        Assertions.assertNotNull(client);
    }

    @Test
    void testGetCryptoAsyncClientShouldReturnClientWhenCalled() {
        //given

        //when
        final CryptographyAsyncClient client = underTest.getCryptoAsyncClient(WEB_KEY_ID);

        //then
        Assertions.assertNotNull(client);
    }

    @Test
    void testGetCryptoClientShouldReturnClientWhenCalled() {
        //given

        //when
        final CryptographyClient client = underTest.getCryptoClient(WEB_KEY_ID);

        //then
        Assertions.assertNotNull(client);
    }
}
