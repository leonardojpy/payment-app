const walletForm = document.querySelector("#wallet-form");
const transactionForm = document.querySelector("#transaction-form");
const walletList = document.querySelector("#wallet-list");
const transactionList = document.querySelector("#transaction-list");
const payerSelect = document.querySelector("#payer-select");
const payeeSelect = document.querySelector("#payee-select");
const successMessage = document.querySelector("#success-message");
const errorMessage = document.querySelector("#error-message");
const walletCount = document.querySelector("#wallet-count");
const transactionCount = document.querySelector("#transaction-count");
const totalBalance = document.querySelector("#total-balance");
const refreshWalletsButton = document.querySelector("#refresh-wallets");
const refreshTransactionsButton = document.querySelector("#refresh-transactions");

const state = {
    wallets: [],
    transactions: []
};

async function request(path, options = {}) {
    const response = await fetch(path, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (response.status === 204) {
        return null;
    }

    const text = await response.text();
    const body = text ? JSON.parse(text) : null;

    if (!response.ok) {
        throw new Error(parseError(body));
    }

    return body;
}

function parseError(body) {
    if (!body) {
        return "Nao foi possivel concluir a operacao.";
    }

    if (body.detail) {
        return body.detail;
    }

    if (body.errors) {
        return Object.entries(body.errors)
            .map(([field, message]) => `${field}: ${message}`)
            .join(" | ");
    }

    if (body.title) {
        return body.title;
    }

    return "Nao foi possivel concluir a operacao.";
}

function showSuccess(message) {
    successMessage.textContent = message;
    successMessage.classList.remove("hidden");
    errorMessage.classList.add("hidden");
}

function showError(message) {
    errorMessage.textContent = message;
    errorMessage.classList.remove("hidden");
    successMessage.classList.add("hidden");
}

function clearFeedback() {
    successMessage.classList.add("hidden");
    errorMessage.classList.add("hidden");
}

function formatCurrency(value) {
    return new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    }).format(Number(value || 0));
}

function formatDate(value) {
    if (!value) {
        return "-";
    }

    return new Intl.DateTimeFormat("pt-BR", {
        dateStyle: "short",
        timeStyle: "short"
    }).format(new Date(value));
}

function renderStats() {
    walletCount.textContent = String(state.wallets.length);
    transactionCount.textContent = String(state.transactions.length);

    const balance = state.wallets.reduce((sum, wallet) => sum + Number(wallet.balance || 0), 0);
    totalBalance.textContent = formatCurrency(balance);
}

function renderWalletOptions() {
    const options = state.wallets.map((wallet) => {
        const label = `${wallet.id} - ${wallet.fullName} (${wallet.walletType})`;
        return `<option value="${wallet.id}">${label}</option>`;
    }).join("");

    const placeholder = `<option value="" disabled selected>Selecione uma carteira</option>`;
    payerSelect.innerHTML = placeholder + options;
    payeeSelect.innerHTML = placeholder + options;
}

function renderWallets() {
    if (!state.wallets.length) {
        walletList.innerHTML = `<div class="empty-state">Nenhuma carteira cadastrada ainda.</div>`;
        renderWalletOptions();
        renderStats();
        return;
    }

    walletList.innerHTML = state.wallets.map((wallet) => `
        <article class="list-item">
            <div class="list-item-header">
                <strong>${wallet.fullName}</strong>
                <span class="wallet-type ${wallet.walletType.toLowerCase()}">${wallet.walletType}</span>
            </div>
            <div class="muted">ID ${wallet.id} - ${wallet.email}</div>
            <div class="list-item-footer">
                <span class="muted">${wallet.cpfCnpj}</span>
                <span class="amount">${formatCurrency(wallet.balance)}</span>
            </div>
        </article>
    `).join("");

    renderWalletOptions();
    renderStats();
}

function renderTransactions() {
    if (!state.transactions.length) {
        transactionList.innerHTML = `<div class="empty-state">Nenhuma transacao registrada ainda.</div>`;
        renderStats();
        return;
    }

    transactionList.innerHTML = state.transactions.map((transaction) => `
        <article class="list-item">
            <div class="list-item-header">
                <strong>Transacao #${transaction.id}</strong>
                <span class="transaction-status ${transaction.status.toLowerCase()}">${transaction.status}</span>
            </div>
            <div class="muted">Pagador ${transaction.payerId} -> Recebedor ${transaction.payeeId}</div>
            <div class="list-item-footer">
                <span class="muted">${formatDate(transaction.createdAt)}</span>
                <span class="amount">${formatCurrency(transaction.value)}</span>
            </div>
        </article>
    `).join("");

    renderStats();
}

async function loadWallets() {
    state.wallets = await request("/wallets");
    renderWallets();
}

async function loadTransactions() {
    state.transactions = await request("/transactions");
    renderTransactions();
}

async function refreshAll() {
    const [wallets, transactions] = await Promise.all([
        request("/wallets"),
        request("/transactions")
    ]);

    state.wallets = wallets;
    state.transactions = transactions;

    renderWallets();
    renderTransactions();
}

walletForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    clearFeedback();

    const formData = new FormData(walletForm);
    const payload = Object.fromEntries(formData.entries());
    payload.balance = Number(payload.balance);

    const submitButton = walletForm.querySelector("button[type='submit']");
    submitButton.disabled = true;

    try {
        await request("/wallets", {
            method: "POST",
            body: JSON.stringify(payload)
        });

        walletForm.reset();
        showSuccess("Carteira criada com sucesso.");
        await refreshAll();
    } catch (error) {
        showError(error.message);
    } finally {
        submitButton.disabled = false;
    }
});

transactionForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    clearFeedback();

    const formData = new FormData(transactionForm);
    const payload = Object.fromEntries(formData.entries());
    payload.payer = Number(payload.payer);
    payload.payee = Number(payload.payee);
    payload.value = Number(payload.value);

    const submitButton = transactionForm.querySelector("button[type='submit']");
    submitButton.disabled = true;

    try {
        await request("/transactions", {
            method: "POST",
            body: JSON.stringify(payload)
        });

        transactionForm.reset();
        showSuccess("Transferencia realizada com sucesso.");
        await refreshAll();
    } catch (error) {
        showError(error.message);
    } finally {
        submitButton.disabled = false;
    }
});

refreshWalletsButton.addEventListener("click", async () => {
    clearFeedback();

    try {
        await loadWallets();
        showSuccess("Carteiras atualizadas.");
    } catch (error) {
        showError(error.message);
    }
});

refreshTransactionsButton.addEventListener("click", async () => {
    clearFeedback();

    try {
        await loadTransactions();
        showSuccess("Transacoes atualizadas.");
    } catch (error) {
        showError(error.message);
    }
});

refreshAll().catch((error) => {
    showError(error.message);
});
