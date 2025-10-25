# AI-Powered Personal Finance Assistant

![Status](https://img.shields.io/badge/Status-In%20Active%20Development-blue)
![Tech](https://img.shields.io/badge/Tech-Python%20%7C%20LangGraph%20%7C%20React%20Native-green)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

An intelligent, India-first financial planning application designed to help users calculate their true cost of living and achieve financial independence (FI).

This project moves beyond simple expense tracking to calculate a hyper-personalized, **inflation-adjusted "Target Corpus"** and provides actionable, multi-agent AI-driven advice to help users reach their financial goals (e.g., FIRE, home ownership, debt-free).

## ⚠️ Current Status: In Active Development

This repository contains the foundational architecture, backend logic, and AI systems for the application. It is currently being actively built. The core backend engine is functional, and work is underway to integrate the frontend and the Account Aggregator pipeline.

## ✨ Core Features (Planned)

* **RBI Account Aggregator (AA) Integration:** Secure, consent-based ingestion of financial data (bank transactions, investments) compliant with the **DPDP Act 2023**.
* **Inflation-Compounded Corpus Engine:** Calculates a user's true Target Corpus by projecting future expenses using official **MoSPI CPI data**, not a naive fixed-rate model.
* **GST-Aware Expense Analysis:** An NLP/OCR pipeline to parse bank/bill data, perform **reverse-GST calculations** for accurate consumption modeling, and categorize expenses using a fine-tuned **FinBERT-Indic** model.
* **Multi-Agent Financial Planner:** A premium feature using a **LangGraph**-based system (with SLMs like Phi-3 & Mistral) to act as a Savings Planner, Investment Strategist, and Behavioral Coach, all trained on Indian financial context (SEBI, NPS, PPF).
* **"Real Hourly Wage" Calculator:** A unique UI/UX feature to help users re-frame their spending habits based on their true, localized cost of time.

## 🛠️ Technical Architecture

The system is designed as a decoupled, AI-first service:

[React Native App] --> [FastAPI Backend (Python)] --> [PostgreSQL DB] | | | v v v [Acct. Aggregator API] [LangGraph Multi-Agent System] [ChromaDB (Vectors)] | | v v [NLP Pipeline (FinBERT)] [SLMs (Phi-3, Mistral)]
