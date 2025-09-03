# CargaLink CDMX - Backend

Backend service for **CargaLink CDMX**, a web application developed as part of a **Bachelor’s Thesis at ESCOM (IPN)**.  
The system connects small and medium-sized freight transport companies with businesses requiring cargo services in Mexico City.  

This repository contains the **Spring Boot backend** that powers the platform, offering a secure and scalable API to manage users, companies, cargos, trips, and AI-based services.

---

## Project Overview

CargaLink CDMX streamlines the process of finding and hiring freight transport services.  
It reduces the dependence on intermediaries, promotes transparency, and fosters fair competition among transport companies.  

---

## User Roles

The system supports **role-based access control (RBAC)** with four main roles:

1. **Administrator**  
   - Full access to the platform.  
   - Manages all users, companies, cargos, and trips.  

2. **Transport Company Representative (RepTrans)**  
   - Manages the company, transportists, trucks, and trailers.  
   - Applies to cargos published by clients.  
   - Configures trips by assigning resources (driver, truck, semi-trailer).  
   - Handles unexpected issues (accidents, problems) and reallocates resources.  

3. **Client Representative (RepCli)**  
   - Publishes cargo offers to be transported (currently limited to *general cargo*).  
   - Cannot request hazardous materials, construction materials, or liquids (*future work*).  
   - Selects and contracts transport companies directly.  

4. **Transportist (Driver)**  
   - Executes assigned trips.  
   - Updates trip status: *scheduled → picking up → loading → en route → delivering → delivered*.  
   - Can report problems during trips.  
   - Communicates with other users via the chat module.  

---

## Features

- **Authentication & Security**  
  Implemented with **Spring Security + JWT**.

- **Cargo Management**  
  Clients publish offers; transport companies apply and configure trips in compliance with **NOM-012-SCT-2-2017**.

- **Trip Lifecycle**  
  Transportists update trip states.

- **Chat Module**  
  - RepTrans ↔ Transportists  
  - RepCli ↔ RepTrans  
  - Transportists ↔ Transportists (same company)  
  *(Currently with some bugs where messages may not always be delivered)*  

- **Ratings System**  
  Clients can rate transport companies based on service quality and leave comments explaining the experience with the company and transportist.

- **Recommendation System**  
  AI-powered recommendations using **cosine similarity and preference vectors** to match clients with suitable transport companies.

- **Chatbot Support**  
  FAQ chatbot using **NLP** to assist users and reduce technology adoption barriers.

- **Payments**  
  Payment flow is **simulated** (future integration with real payment gateways).  

---

## Tech Stack

- **Backend Framework:** Spring Boot (Java)
- **Frontend Frameword:** Angular (TypeScript)
- **Security:** Spring Security + JWT  
- **Database:** MySQL
- **Build Tool:** Maven / Gradle  

---
## Future work
- Extend cargo types (hazardous materials, liquids, construction).
- Integrate real payment gateway.
- Improve chat reliability and scalability.
- Enhance recommendation system with advanced ML models.
- Expand coverage to all of Mexico.
- Modules: Rent Truck, Rent Trailer, Contract Transportist

---
## Contact
Developed as a Bachelor’s Thesis project at the Escuela Superior de Cómputo (ESCOM - IPN)
Under the supervision of M. en C. Gabriela de Jesús López Ruíz.
-Developers:
  - [Alejandre Dominguez Alan José](https://github.com/H4d3rach)
  - [Estanislao Castro Ismael](https://github.com/Shutman-ZTAY)
  - [Gil Calderón Karla]()

