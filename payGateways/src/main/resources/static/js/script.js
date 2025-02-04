function processPayment(gateway) {

    // Validate form data
    if (!validateForm()) {
        return; // Abort if validation fails
    }

    // Collect form data
    const cardNumber = document.getElementById('cardNumber').value;
    const expiryDate = document.getElementById('expiryDate').value;
    const cvv = document.getElementById('cvv').value;
    const amount = document.getElementById('amount').value;

    // Prepare data to send
    const paymentData = {
        gateway: gateway,
        cardDetails: {
            number: cardNumber,
            expiry: expiryDate,
            cvv: cvv
        },
        amount: amount
    };

    const encryptedData = encryptData(paymentData);

    // Call the backend
    fetch(`/api/card/payments/process`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ data: encryptedData }),
    })
    .then(response => response.json())
    .then(result => {
        alert(`Payment Status: ${result.status}`);
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function validateCardNumber() {
    const cardNumber = document.getElementById('cardNumber').value.replace(/\s+/g, '');
    const cardNumberPattern = /^\d{16}$/;
    const errorElement = document.getElementById('cardNumberError');

    if (!cardNumberPattern.test(cardNumber)) {
        errorElement.style.display = 'inline';
        return false;
    } else {
        errorElement.style.display = 'none';
        return true;
    }
}

function validateExpiryDate() {
    const expiryDate = document.getElementById('expiryDate').value;
    const expiryDatePattern = /^\d{2}\/\d{4}$/;
    const errorElement = document.getElementById('expiryDateError');

    if (!expiryDatePattern.test(expiryDate)) {
        errorElement.style.display = 'inline';
        return false;
    } else {
        errorElement.style.display = 'none';
        return true;
    }
}

function validateCVV() {
    const cvv = document.getElementById('cvv').value;
    const cvvPattern = /^\d{3}$/;
    const errorElement = document.getElementById('cvvError');

    if (!cvvPattern.test(cvv)) {
        errorElement.style.display = 'inline';
        return false;
    } else {
        errorElement.style.display = 'none';
        return true;
    }
}

function validateAmount() {
    const amount = document.getElementById('amount').value;
    const errorElement = document.getElementById('amountError');

    if (amount <= 0) {
        errorElement.style.display = 'inline';
        return false;
    } else {
        errorElement.style.display = 'none';
        return true;
    }
}

function validateForm() {
    const isCardNumberValid = validateCardNumber();
    const isExpiryDateValid = validateExpiryDate();
    const isCVVValid = validateCVV();
    const isAmountValid = validateAmount();

    return isCardNumberValid && isExpiryDateValid && isCVVValid && isAmountValid;
}

function encryptData(data) {
    const key = CryptoJS.enc.Hex.parse('6a0fcbf6ba755ffb14d0ab4e1bda6c7dd8f309abf2f684fa4f9c1c91d25fbc9b'); // 256-bit key
    const iv = CryptoJS.enc.Hex.parse('a3e1c1a2f7b1c9d9f0c6a2d3b4e1a1f9'); // 128-bit IV
    const encrypted = CryptoJS.AES.encrypt(JSON.stringify(data), key, { iv: iv });
    return encrypted.toString();
}

function formatCardNumber() {
    const cardNumber = document.getElementById('cardNumber');
    cardNumber.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, ''); // Remove non-digits
        if (value.length > 4) {
            value = value.match(/.{1,4}/g).join(' ');
        }
        e.target.value = value;
    });
}

function formatExpiryDate() {
    const expiryDate = document.getElementById('expiryDate');
    expiryDate.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, ''); // Remove non-digits
        if (value.length > 2) {
            value = value.slice(0, 2) + '/' + value.slice(2);
        }
        e.target.value = value;
    });
}

// Initialize formatting functions
document.addEventListener('DOMContentLoaded', function() {
    formatCardNumber();
    formatExpiryDate();
    document.getElementById('cardNumber').addEventListener('input', validateCardNumber);
    document.getElementById('expiryDate').addEventListener('input', validateExpiryDate);
    document.getElementById('cvv').addEventListener('input', validateCVV);
    document.getElementById('amount').addEventListener('input', validateAmount);
});

