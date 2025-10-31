# ☕ Coffee Shop Application – Documentation

## 🔧 Project Tools

To run and work with this project, ensure the following is installed:

- **Java Development Kit (JDK): Version 21**
- **Eclipse IDE** (or any compatible Java IDE)

---

## 📦 Project Import Instructions (Eclipse)

To import the project into Eclipse:

1. Download the project files:
   - **Stage 1** – [CoffeeShopGroup20Codes.zip](https://gitlab-student.macs.hw.ac.uk/f21as-group-20/coffee-shop-group-20/-/raw/master/CoffeeShopGroup20Codes.zip)  
   - **Stage 2** – [Link Missing]

2. In Eclipse:
   - Go to **File > Import > General > Existing Projects into Workspace**
   - Choose **Select archive file**
   - Locate and select the downloaded `.zip` file
   - Click **Finish**

---

## 🗂 Project Structure

The project contains the following main directories:

1. `src/main/java` – Contains the main application logic and Java classes  
2. `src/main/resources` – Stores application-level resources  
3. `src/test/java` – Contains unit and integration test classes  
4. `data` – Application data files (users, orders, menu items, etc.)  
5. `logs` – Contains daily-generated log files

---

## 📁 Package Structure

**Root Package:** `uk.ac.hw.group20`

### Modular Packages:

| Package | Description |
|--------|-------------|
| `uk.ac.hw.group20.admin` | Handles user identity management |
| `uk.ac.hw.group20.errorhandler` | Manages custom exceptions |
| `uk.ac.hw.group20.main` | Main application logic and GUI |
| `uk.ac.hw.group20.order` | Order creation and management |
| `uk.ac.hw.group20.order.bill` | Billing-related functionality |
| `uk.ac.hw.group20.report` | Report generation |
| `uk.ac.hw.group20.utils` | Utility/helper functions |
| `uk.ac.hw.group20.logger` | Logger setup and functionality |

---

## ⚙️ Installation & Running the Application

1. Download the appropriate runnable `.jar` files:
   - **Stage 1** – [Download](https://gitlab-student.macs.hw.ac.uk/f21as-group-20/coffee-shop-group-20/-/blob/master/F21AS_Edinburgh_Group_20_Runnable_Jar_Stage_1.zip)
   - **Stage 2** – [Download](https://gitlab-student.macs.hw.ac.uk/f21as-group-20/coffee-shop-group-20/-/blob/master/F21AS_Edinburgh_Group_20_Runnable_Jar_Stage_2.zip)

2. Extract the zip file to a preferred location  
3. Ensure the extracted folder contains:
   - A `.jar` file
   - `data` folder
   - `logs` folder  
4. Double-click the `.jar` file to run the application  
5. Log in using:
   - **Username:** `IMA`
   - **Password:** `IMA`  
   *(Case-sensitive)*  
6. Use the on-screen interface to interact with the system

---

## 🛒 Features Overview

### ✔️ Placing an Order
1. Choose a category (Beverage, Food, Other Items)
2. Select the desired item
3. Adjust quantity if needed
4. Click **Add Item** to add it to the cart
5. Repeat as necessary to complete the order
6. System will update quantity for repeated items
7. Click **Complete and Pay** to finalize the order

---

### 🧾 Processing Orders
1. Navigate to **Orders > Process**
2. Orders will appear in the Information Panel
3. Click **Process Order** to start processing
4. Review order details and confirm
5. Click **Yes** to confirm or **No** to cancel
6. Processed orders move to the archive

---

### 📊 Reporting
1. Navigate to **Report > Orders**
2. View a summary of all completed orders
3. See quantity and sales aggregation

---

## 👤 User Roles (Case-Sensitive)

| Role | Username | Password |
|------|----------|----------|
| Customer | `IMA` | `IMA` |
| Employee | `admin` | `password` |