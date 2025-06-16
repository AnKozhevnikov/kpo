document.addEventListener('DOMContentLoaded', () => {
    // === Configuration ===
    // IMPORTANT: Set this to the base URL where your backend payments service is accessible.
    // If running Spring Boot in Docker mapped to localhost:8080, this is likely correct.
    const PAYMENTS_SERVICE_BASE_URL = 'http://localhost:8080/payments-service'; 
    // =====================

    // Create Account elements
    const createAccountName = document.getElementById('createAccountName');
    const createAccountBtn = document.getElementById('createAccountBtn');
    const createAccountOutput = document.getElementById('createAccountOutput');

    // Deposit Money elements
    const depositAccountId = document.getElementById('depositAccountId');
    const depositAmount = document.getElementById('depositAmount');
    const depositBtn = document.getElementById('depositBtn');
    const depositOutput = document.getElementById('depositOutput');

    // Get Account Info elements
    const getAccountInfoId = document.getElementById('getAccountInfoId');
    const getAccountInfoBtn = document.getElementById('getAccountInfoBtn');
    const getAccountInfoOutput = document.getElementById('getAccountInfoOutput');

    // Order Management elements
    const createOrderUserId = document.getElementById('createOrderUserId');
    const createOrderAmount = document.getElementById('createOrderAmount');
    const createOrderDescription = document.getElementById('createOrderDescription');
    const createOrderBtn = document.getElementById('createOrderBtn');
    const createOrderOutput = document.getElementById('createOrderOutput');

    const getOrdersByUserId = document.getElementById('getOrdersByUserId');
    const getOrdersBtn = document.getElementById('getOrdersBtn');
    const getOrdersOutput = document.getElementById('getOrdersOutput');

    const getOrderStatusId = document.getElementById('getOrderStatusId');
    const getOrderStatusBtn = document.getElementById('getOrderStatusBtn');
    const getOrderStatusOutput = document.getElementById('getOrderStatusOutput');

    // Function to display output
    const displayOutput = (outputElement, message, isError = false) => {
        outputElement.textContent = message;
        outputElement.style.color = isError ? 'red' : 'green';
        outputElement.style.borderColor = isError ? '#dc3545' : '#28a745';
        outputElement.style.backgroundColor = isError ? '#f8d7da' : '#d4edda';
    };

    // Create Account
    createAccountBtn.addEventListener('click', async () => {
        const name = createAccountName.value;
        if (!name) {
            displayOutput(createAccountOutput, 'Please enter an account name.', true);
            return;
        }

        createAccountOutput.textContent = 'Creating account...';
        try {
            const formData = new URLSearchParams();
            formData.append('name', name);

            const response = await fetch(`${PAYMENTS_SERVICE_BASE_URL}/account`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData.toString()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
            }

            const accountId = await response.text(); // Expecting a Long ID back
            displayOutput(createAccountOutput, `Account created successfully! ID: ${accountId}`);
            createAccountName.value = ''; // Clear input
        } catch (error) {
            displayOutput(createAccountOutput, `Error creating account: ${error.message}`, true);
            console.error('Error:', error);
        }
    });

    // Deposit Money
    depositBtn.addEventListener('click', async () => {
        const accountId = depositAccountId.value;
        const amount = depositAmount.value;

        if (!accountId || !amount) {
            displayOutput(depositOutput, 'Please enter both Account ID and Amount.', true);
            return;
        }

        depositOutput.textContent = 'Depositing money...';
        try {
            const formData = new URLSearchParams();
            formData.append('accountId', accountId);
            formData.append('amount', amount);

            const response = await fetch(`${PAYMENTS_SERVICE_BASE_URL}/account/deposit`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData.toString()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
            }

            displayOutput(depositOutput, 'Deposit successful!');
            depositAccountId.value = ''; // Clear inputs
            depositAmount.value = '';
        } catch (error) {
            displayOutput(depositOutput, `Error depositing money: ${error.message}`, true);
            console.error('Error:', error);
        }
    });

    // Get Account Info
    getAccountInfoBtn.addEventListener('click', async () => {
        const accountId = getAccountInfoId.value;
        if (!accountId) {
            displayOutput(getAccountInfoOutput, 'Please enter an Account ID.', true);
            return;
        }

        getAccountInfoOutput.textContent = 'Fetching account info...';
        try {
            const response = await fetch(`${PAYMENTS_SERVICE_BASE_URL}/account/info?accountId=${encodeURIComponent(accountId)}`);

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
            }
            const data = await response.json();
            displayOutput(getAccountInfoOutput, JSON.stringify(data, null, 2));
            getAccountInfoId.value = ''; // Clear input
        } catch (error) {
            displayOutput(getAccountInfoOutput, `Error fetching account info: ${error.message}`, true);
            console.error('Error:', error);
        }
    });

    // Order Management Functions
    const ORDERS_SERVICE_BASE_URL = 'http://localhost:8080/orders-service'; // Base URL for Orders Service

    // Create Order
    createOrderBtn.addEventListener('click', async () => {
        const userId = createOrderUserId.value;
        const amount = createOrderAmount.value;
        const description = createOrderDescription.value;

        if (!userId || !amount || !description) {
            displayOutput(createOrderOutput, 'Please fill all fields for creating an order.', true);
            return;
        }

        createOrderOutput.textContent = 'Creating order...';
        try {
            const formData = new URLSearchParams();
            formData.append('userId', userId);
            formData.append('amount', amount);
            formData.append('description', description);

            const response = await fetch(`${ORDERS_SERVICE_BASE_URL}/order`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData.toString()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
            }

            const orderId = await response.text();
            displayOutput(createOrderOutput, `Order created successfully! ID: ${orderId}`);
            createOrderUserId.value = '';
            createOrderAmount.value = '';
            createOrderDescription.value = '';
        } catch (error) {
            displayOutput(createOrderOutput, `Error creating order: ${error.message}`, true);
            console.error('Error:', error);
        }
    });

    // Get Orders by User ID
    getOrdersBtn.addEventListener('click', async () => {
        const userId = getOrdersByUserId.value;
        if (!userId) {
            displayOutput(getOrdersOutput, 'Please enter a User ID.', true);
            return;
        }

        getOrdersOutput.textContent = 'Fetching orders...';
        try {
            // Note: The endpoint is /orders/{userId}, but Spring often maps @RequestParam from path vars as well if defined in controller
            const response = await fetch(`${ORDERS_SERVICE_BASE_URL}/orders/${encodeURIComponent(userId)}`);

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
            }
            const data = await response.json();
            displayOutput(getOrdersOutput, JSON.stringify(data, null, 2));
            getOrdersByUserId.value = '';
        } catch (error) {
            displayOutput(getOrdersOutput, `Error fetching orders: ${error.message}`, true);
            console.error('Error:', error);
        }
    });

    // Get Order Status by ID
    getOrderStatusBtn.addEventListener('click', async () => {
        const orderId = getOrderStatusId.value;
        if (!orderId) {
            displayOutput(getOrderStatusOutput, 'Please enter an Order ID.', true);
            return;
        }

        getOrderStatusOutput.textContent = 'Fetching order status...';
        try {
            const response = await fetch(`${ORDERS_SERVICE_BASE_URL}/order/status/${encodeURIComponent(orderId)}`);

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
            }
            const data = await response.text(); // Expecting a String (OrderStatus.name())
            displayOutput(getOrderStatusOutput, `Order Status: ${data}`);
            getOrderStatusId.value = '';
        } catch (error) {
            displayOutput(getOrderStatusOutput, `Error fetching order status: ${error.message}`, true);
            console.error('Error:', error);
        }
    });
}); 