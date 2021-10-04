package com.github.nagyesta.lowkeyvault.model.v7_2.key.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.nagyesta.lowkeyvault.service.key.ReadOnlyAesKeyVaultKeyEntity;
import com.github.nagyesta.lowkeyvault.service.key.ReadOnlyEcKeyVaultKeyEntity;
import com.github.nagyesta.lowkeyvault.service.key.ReadOnlyKeyVaultKeyEntity;
import com.github.nagyesta.lowkeyvault.service.key.ReadOnlyRsaKeyVaultKeyEntity;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Defines values for KeyType.
 */
@SuppressWarnings("checkstyle:MagicNumber")
public enum KeyType {

    /**
     * Static value EC for KeyType.
     */
    EC("EC") {
        @Override
        public boolean isEc() {
            return true;
        }

        @Override
        public <E> SortedSet<E> getValidKeyParameters(final Class<E> itemType) {
            Assert.isTrue(KeyCurveName.class.equals(itemType), "Key parameter is KeyCurveName for EC.");
            return Arrays.stream(KeyCurveName.values())
                    .map(itemType::cast)
                    .collect(Collectors.toCollection(TreeSet::new));
        }
    },

    /**
     * Static value EC-HSM for KeyType.
     */
    EC_HSM("EC-HSM") {
        @Override
        public boolean isEc() {
            return true;
        }

        @Override
        public boolean isHsm() {
            return true;
        }

        @Override
        public <E> SortedSet<E> getValidKeyParameters(final Class<E> itemType) {
            return EC.getValidKeyParameters(itemType);
        }
    },

    /**
     * Static value RSA for KeyType.
     */
    RSA("RSA") {
        @Override
        public boolean isRsa() {
            return true;
        }

        @Override
        public <E> SortedSet<E> getValidKeyParameters(final Class<E> itemType) {
            Assert.isTrue(Integer.class.equals(itemType), "Key parameter is Integer for RSA.");
            return Stream.of(2048, 3072, 4096)
                    .map(itemType::cast)
                    .collect(Collectors.toCollection(TreeSet::new));
        }
    },

    /**
     * Static value RSA-HSM for KeyType.
     */
    RSA_HSM("RSA-HSM") {
        @Override
        public boolean isRsa() {
            return true;
        }

        @Override
        public boolean isHsm() {
            return true;
        }

        @Override
        public <E> SortedSet<E> getValidKeyParameters(final Class<E> itemType) {
            return RSA.getValidKeyParameters(itemType);
        }
    },

    /**
     * Static value oct for KeyType.
     */
    OCT("oct") {
        @Override
        public boolean isOct() {
            return true;
        }

        @Override
        public <E> SortedSet<E> getValidKeyParameters(final Class<E> itemType) {
            Assert.isTrue(Integer.class.equals(itemType), "Key parameter is Integer for OCT.");
            return Stream.of(128, 192, 256)
                    .map(itemType::cast)
                    .collect(Collectors.toCollection(TreeSet::new));
        }
    },

    /**
     * Static value oct-HSM for KeyType.
     */
    OCT_HSM("oct-HSM") {
        @Override
        public boolean isOct() {
            return true;
        }

        @Override
        public boolean isHsm() {
            return true;
        }

        @Override
        public <E> SortedSet<E> getValidKeyParameters(final Class<E> itemType) {
            return OCT.getValidKeyParameters(itemType);
        }
    };

    private final String value;

    KeyType(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static KeyType forValue(final String name) {
        return Arrays.stream(values()).filter(keyType -> keyType.getValue().equals(name)).findFirst().orElse(null);
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonIgnore
    public boolean isRsa() {
        return false;
    }

    @JsonIgnore
    public boolean isEc() {
        return false;
    }

    @JsonIgnore
    public boolean isOct() {
        return false;
    }

    @JsonIgnore
    public boolean isHsm() {
        return false;
    }

    @JsonIgnore
    public String getAlgorithmName() {
        if (isEc()) {
            return "EC";
        } else if (isRsa()) {
            return "RSA";
        } else {
            Assert.isTrue(isOct(), "Unknown key type found: " + this);
            return "AES";
        }
    }

    @JsonIgnore
    public abstract <E> SortedSet<E> getValidKeyParameters(Class<E> itemType);

    @JsonIgnore
    public Class<? extends ReadOnlyKeyVaultKeyEntity> entityClass() {
        if (isRsa()) {
            return ReadOnlyRsaKeyVaultKeyEntity.class;
        } else if (isEc()) {
            return ReadOnlyEcKeyVaultKeyEntity.class;
        } else {
            Assert.isTrue(isOct(), "Unknown key type found: " + this);
            return ReadOnlyAesKeyVaultKeyEntity.class;
        }
    }

    public <E> void validate(final E value, final Class<E> type) {
        final SortedSet<E> validKeyParameters = getValidKeyParameters(type);
        Assert.isTrue(value == null || validKeyParameters.contains(value),
                "Invalid value provided: " + value + " valid values are: " + validKeyParameters);
    }

    public <E> E validateOrDefault(final E value, final Class<E> type) {
        validate(value, type);
        return Objects.requireNonNullElse(value, getValidKeyParameters(type).first());
    }
}
