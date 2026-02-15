import secrets
import string
from flask import Flask, request, jsonify, render_template
from toolsdata import CATEGORIES

app = Flask(__name__)

@app.context_processor
def inject_categories():
    return dict(categories=CATEGORIES)

@app.route("/")
def dashboard():
    return render_template("dashboard.html", page_type="dashboard")

@app.route("/password-generator", methods=["GET"])
def password_generator():
    return render_template("tools/password_generator.html", page_type="tool")



@app.route("/api/password-generator", methods=["POST"])
def api_password_generator():
    data = request.json

    length = int(data.get("length", 16))
    upper = data.get("upper", True)
    lower = data.get("lower", True)
    digits = data.get("digits", True)
    symbols = data.get("symbols", True)

    chars = ""
    if upper:
        chars += string.ascii_uppercase
    if lower:
        chars += string.ascii_lowercase
    if digits:
        chars += string.digits
    if symbols:
        chars += "!@#$%^&*()_+-=[]{}|;:,.<>?"

    if not chars:
        return jsonify({"password": ""})

    password = "".join(secrets.choice(chars) for _ in range(length))
    return jsonify({"password": password})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)

