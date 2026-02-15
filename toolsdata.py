# tools_data.py
# Single source of truth for all categories & tools

CATEGORIES = [
    {
        "id": "security",
        "title": "🔐 Admin / Admin (Security & Auth)",
        "tools": [
            {
                "slug": "password-generator",
                "title": "Password Generator",
                "description": "Generate strong and secure passwords",
                "icon": "key",
                "color": "text-cyan-400",
            },
            {
                "slug": "password-checker",
                "title": "Password Strength Checker",
                "description": "Check password strength instantly",
                "icon": "lock",
                "color": "text-yellow-400",
            },
            {
                "slug": "password-manager",
                "title": "Password Manager",
                "description": "Store and manage passwords securely",
                "icon": "shield",
                "color": "text-green-400",
            },
            {
                "slug": "hash-generator",
                "title": "Hash Generator",
                "description": "Generate hashes for text and data",
                "icon": "hash",
                "color": "text-purple-400",
            },
            {
                "slug": "token-generator",
                "title": "Random Token Generator",
                "description": "Generate secure random tokens",
                "icon": "shuffle",
                "color": "text-pink-400",
            },
        ],
    },

    {
        "id": "converters",
        "title": "🔄 Universal Translator (Converters)",
        "tools": [
            {
                "slug": "unit-converter",
                "title": "Unit Converter",
                "description": "Convert measurement units easily",
                "icon": "ruler",
                "color": "text-orange-400",
            },
            {
                "slug": "currency-converter",
                "title": "Currency Converter",
                "description": "Convert between world currencies",
                "icon": "refresh-cw",
                "color": "text-teal-400",
            },
            {
                "slug": "time-zone-converter",
                "title": "Time Zone Converter",
                "description": "Convert time zones globally",
                "icon": "clock",
                "color": "text-blue-400",
            },
            {
                "slug": "digital-size-converter",
                "title": "Digital Size Converter",
                "description": "Convert KB, MB, GB and more",
                "icon": "database",
                "color": "text-pink-400",
            },
            {
                "slug": "number-base-converter",
                "title": "Number Base Converter",
                "description": "Binary, decimal, hexadecimal conversions",
                "icon": "binary",
                "color": "text-purple-400",
            },
            {
                "slug": "temperature-converter",
                "title": "Temperature Converter",
                "description": "Convert between Celsius, Fahrenheit & Kelvin",
                "icon": "thermometer",
                "color": "text-red-400",
            },
        ],
    },

    {
        "id": "dev",
        "title": "🧪 It Works on My Machine (Developer Utilities)",
        "tools": [
            {
                "slug": "json-formatter",
                "title": "JSON Formatter",
                "description": "Format and validate JSON data",
                "icon": "braces",
                "color": "text-green-400",
            },
            {
                "slug": "base64-tool",
                "title": "Base64 Encoder / Decoder",
                "description": "Encode and decode Base64 strings",
                "icon": "binary",
                "color": "text-cyan-400",
            },
            {
                "slug": "sql-formatter",
                "title": "SQL Formatter",
                "description": "Format SQL queries cleanly",
                "icon": "file-text",
                "color": "text-purple-400",
            },
            {
                "slug": "jwt-debugger",
                "title": "JWT Debugger",
                "description": "Decode and inspect JWT tokens",
                "icon": "key-round",
                "color": "text-yellow-400",
            },
            {
                "slug": "uuid-generator",
                "title": "UUID Generator",
                "description": "Generate unique identifiers",
                "icon": "hash",
                "color": "text-pink-400",
            },
        ],
    },

    {
        "id": "finance",
        "title": "💸 What Am I Broke (Finance)",
        "tools": [
            {
                "slug": "expense-tracker",
                "title": "Expense Tracker",
                "description": "Track your daily expenses",
                "icon": "wallet",
                "color": "text-emerald-400",
            },
            {
                "slug": "emi-calculator",
                "title": "EMI / Loan Calculator",
                "description": "Calculate loan EMIs easily",
                "icon": "calculator",
                "color": "text-orange-400",
            },
            {
                "slug": "sip-calculator",
                "title": "SIP Calculator",
                "description": "Estimate investment returns",
                "icon": "trending-up",
                "color": "text-teal-400",
            },
            {
                "slug": "subscription-manager",
                "title": "Subscription Manager",
                "description": "Manage recurring subscriptions",
                "icon": "repeat",
                "color": "text-pink-400",
            },
        ],
    },

    {
        "id": "design",
        "title": "🎨 Pixel Perfect (Design & Images)",
        "tools": [
            {
                "slug": "qr-generator",
                "title": "QR Code Generator",
                "description": "Generate QR codes instantly",
                "icon": "qr-code",
                "color": "text-pink-400",
            },
            {
                "slug": "color-picker",
                "title": "Color Picker",
                "description": "Pick and copy colors",
                "icon": "pipette",
                "color": "text-purple-400",
            },
            {
                "slug": "image-converter",
                "title": "Image Converter",
                "description": "Convert image formats easily",
                "icon": "image",
                "color": "text-blue-400",
            },
        ],
    },

    {
        "id": "text",
        "title": "✍️ The Clipboard Warrior (Text Tools)",
        "tools": [
            {
                "slug": "case-converter",
                "title": "Case Converter",
                "description": "Convert text casing styles",
                "icon": "type",
                "color": "text-cyan-400",
            },
            {
                "slug": "text-cleaner",
                "title": "Text Cleaner",
                "description": "Remove unwanted characters",
                "icon": "eraser",
                "color": "text-yellow-400",
            },
            {
                "slug": "diff-checker",
                "title": "Diff Checker",
                "description": "Compare text differences",
                "icon": "diff",
                "color": "text-red-400",
            },
            {
                "slug": "lorem-ipsum",
                "title": "Lorem Ipsum Generator",
                "description": "Generate placeholder text",
                "icon": "file",
                "color": "text-green-400",
            },
        ],
    },

    {
        "id": "network",
        "title": "🌐 Is It Me or the Internet",
        "tools": [
            {
                "slug": "my-ip",
                "title": "My IP Address",
                "description": "Check your public IP address",
                "icon": "wifi",
                "color": "text-teal-400",
            },
            {
                "slug": "speed-test",
                "title": "Internet Speed Test",
                "description": "Test your network speed",
                "icon": "activity",
                "color": "text-orange-400",
            },
            {
                "slug": "device-info",
                "title": "Device & Screen Info",
                "description": "View device and screen details",
                "icon": "monitor",
                "color": "text-blue-400",
            },
        ],
    },
]
