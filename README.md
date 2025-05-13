# Lumina

# Smart City Management Platform

## Overview

This project was developed as part of the coursework for PIDEV 3A at **Esprit School of Engineering**. It is a Symfony-based smart city management web platform designed to improve municipal services through secure, efficient, and intelligent digital solutions.

The system integrates secure user and document management, real-time traffic and transport monitoring, parking reservations, online payments, and AI-powered assistance — all tailored for modern urban environments.

---

## Features

- **User Management**  
  Secure user registration and login with role-based access and 2-Factor Authentication (2FA).

- **Traffic Management**  
  Real-time monitoring and event-based updates using Kafka and reverse proxy configurations.  
  Includes **AI-powered traffic light optimization** using **FalconAI**.

- **Transport Management**  
  Admin panel to manage public transport schedules, routes.

- **Parking Reservation**  
  Users can reserve city parking spots in advance. A **QR code** is generated and sent via **email confirmation** for access at the location.

- **Municipal Document Management**  
  Upload, preview, and sign documents electronically using the `nutrient-viewer-lib` JavaScript library.

- **Secure Payments**  
  Integration with **Stripe** for secure and seamless service payments.

- **Electronic Signature**  
  Client-side electronic signature for official documents, using `nutrient-viewer-lib` for document interaction and preview.

- **AI Chatbot with Image Upload**  
  Conversational assistant powered by **LLaVA** (Large Language and Vision Assistant) to help users submit and manage municipal documents with **image understanding**.

- **Real-Time Event Streaming**  
  Kafka-powered streaming for city-wide alerts and notifications.

---

## Tech Stack

### Backend

- **Symfony 6.4**
- **PHP 8.1**
- **Doctrine ORM**
- **MySQL / PostgreSQL**
- **API Platform**
- **2FA**
- **Kafka**
- **Stripe PHP SDK**
- **QR Code generation (e.g., Endroid QR Code)**
- **Mailer (Symfony Mailer)**

### Frontend

- **Twig** templating engine  
- **Bootstrap 5** for layout and UI components  
- **Tailwind CSS** for utility-first styling  
- **nutrient-viewer-lib** for document viewing and electronic signing

### AI & NLP

- **FalconAI (Python)** – For intelligent traffic light management
- **LLaVA (Multimodal)** – For chatbot with image upload and understanding

---

## Directory Structure

```text
├── config/
├── public/
│   └── js/ (includes nutrient-viewer-lib)
├── src/
│   ├── Controller/
│   ├── Entity/
│   ├── Service/
│   └── EventSubscriber/
├── templates/
├── assets/
├── var/
├── .env
