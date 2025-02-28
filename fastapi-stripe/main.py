from fastapi import FastAPI, Request, Depends
from fastapi.responses import HTMLResponse, JSONResponse
from fastapi.templating import Jinja2Templates
import stripe
import os
import uvicorn
import logging
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

# Configure logging
logging.basicConfig(
    level=logging.INFO, 
    format="%(asctime)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

logger = logging.getLogger(__name__)

# Initialize FastAPI
app = FastAPI()

# Initialize Stripe API
stripe.api_key = os.getenv("STRIPE_SECRET_KEY", "sk_test_51QuJxcFAOZbhHu8XjNXvmJi1m7U7rzgvkp0r6HQCVnRvy8zPggIu3ty1DecS8xa0Saj9b3so8ClODmbpDC9tGc2I002k56ZSuy")

# Set up Jinja2 templates
templates = Jinja2Templates(directory="templates")


@app.get("/", response_class=HTMLResponse)
async def home(request: Request):
    logger.info("Serving the payment page.")
    return templates.TemplateResponse("index.html", {"request": request})


@app.post("/create-payment-intent")
async def create_payment_intent():
    """Creates a Stripe PaymentIntent and logs details."""
    try:
        logger.info("Creating a new PaymentIntent...")
        intent = stripe.PaymentIntent.create(
            amount=1000,  # Amount in cents ($10.00)
            currency="usd",
            payment_method_types=["card"],
        )
        logger.info(f"PaymentIntent created successfully: ID={intent.id}, Status={intent.status}")
        return {"clientSecret": intent.client_secret}
    except Exception as e:
        logger.error(f"Error creating PaymentIntent: {str(e)}")
        return JSONResponse(content={"error": str(e)}, status_code=400)


@app.post("/webhook")
async def stripe_webhook(request: Request):
    """Handle Stripe webhook events."""
    payload = await request.body()
    sig_header = request.headers.get("stripe-signature")
    webhook_secret = os.getenv("STRIPE_WEBHOOK_SECRET", "your_webhook_secret_here")

    try:
        event = stripe.Webhook.construct_event(payload, sig_header, webhook_secret)
        logger.info(f"Received Stripe event: {event['type']}")

        if event["type"] == "payment_intent.succeeded":
            payment_intent = event["data"]["object"]
            logger.info(f"Payment succeeded: ID={payment_intent['id']}, Amount={payment_intent['amount']} cents")

        elif event["type"] == "payment_intent.payment_failed":
            payment_intent = event["data"]["object"]
            logger.warning(f"Payment failed: ID={payment_intent['id']}, Status={payment_intent['status']}")

        return {"status": "success"}

    except ValueError as e:
        logger.error(f"Webhook error (invalid payload): {e}")
        return JSONResponse(content={"error": "Invalid payload"}, status_code=400)
    except stripe.error.SignatureVerificationError as e:
        logger.error(f"Webhook signature verification failed: {e}")
        return JSONResponse(content={"error": "Signature verification failed"}, status_code=400)


def main():
    """Start the FastAPI application."""
    logger.info("Starting FastAPI server on http://127.0.0.1:8000 🚀")
    uvicorn.run(app, host="127.0.0.1", port=8000)


if __name__ == "__main__":
    main()