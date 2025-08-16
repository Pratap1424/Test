document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("loginForm");
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        const text = await response.text();
        const resDiv = document.getElementById("responseMessage");
        
        if (response.ok) {
            const data = JSON.parse(text);
            resDiv.innerHTML = `
                <p>Successful Login as Userrole as ${data.userType === 'A' ? 'Admin' : 'User'}</p>
                <button onclick="showExistingUser('${data.userType}', '${email}')">Show Existing User</button>
            `;
            // Optionally: Store the userType for later use
            window.sessionStorage.setItem("userType", data.userType);
            window.sessionStorage.setItem("email", email);
        } else {
            resDiv.innerHTML = text;
        }
    });
});

async function showExistingUser(userType, email) {
    const url = userType === 'A' ? '/users/all' : `/users/current?email=${email}`;
    const response = await fetch(url);
    const users = await response.json();

    let html = "<table border='1'><tr><th>First Name</th><th>Last Name</th><th>Email</th><th>Mobile</th></tr>";
    users.forEach(u => {
        html += `<tr><td>${u.firstName}</td><td>${u.lastName}</td><td>${u.email}</td><td>${u.mobileNumber}</td></tr>`;
    });
    html += "</table>";

    if (userType === 'A') {
        html += `<br><button onclick="downloadCSV()">Download CSV</button>`;
    }

    document.getElementById("responseMessage").innerHTML += html;
}

function downloadCSV() {
    window.location.href = "/users/download";
}
