from flask import Flask, request, jsonify
import tensorflow as tf
import os
# import tensorflow.keras
# from tensorflow.keras.models import load_model
from tensorflow.keras.models import load_model
import numpy as np
import pickle


app = Flask(__name__)

model = load_model('model214.h5')

with open('scaler214.pkl', 'rb') as f:
    scaler = pickle.load(f)

@app.route('/process', methods=['POST'])
def process_data():
    try:
        # Mendapatkan data dari request JSON
        data = request.json

        # Mendapatkan nilai input dengan default 0
        laba_kotor = data.get("labaKotor", 0)
        bayar_gaji = data.get("bayarGaji", 0)
        bayar_air = data.get("bayarAir", 0)
        biaya_listrik = data.get("biayaListrik", 0)
        biaya_transport = data.get("biayaTransport", 0)
        biaya_promosi = data.get("biayaPromosi", 0)
        biaya_packaging = data.get("biayaPackaging", 0)
        biaya_pajak = data.get("biayaPajak", 0)

        # Menghitung biaya dan margin
        expenses = bayar_gaji + bayar_air + biaya_listrik + biaya_transport + biaya_promosi + biaya_packaging + biaya_pajak
        revenue = laba_kotor
        net_margin = revenue - expenses
        net_balance = revenue - expenses

        # Menyiapkan data baru untuk prediksi
        new_data = np.array([[laba_kotor, bayar_gaji, bayar_air, biaya_listrik, biaya_transport, biaya_promosi, biaya_packaging, biaya_pajak]])
        new_data_scaled = scaler.transform(new_data)

        # Melakukan prediksi
        prediksi = model.predict(new_data_scaled)
        prediksi_value = float(prediksi[0][0])  # Ambil prediksi pertama, jika model mengembalikan hasil dalam bentuk array 2D

        # Mengembalikan response dalam format JSON
        return jsonify({
            "revenue": revenue,
            "expenses": expenses,
            "net_margin": net_margin,
            "net_balance": net_balance,
            "prediction": prediksi_value
        })
    except Exception as e:
        # Mengembalikan error jika terjadi masalah
        return jsonify({"error": str(e)}), 400

if __name__ == '__main__':
    port = int(os.environ.get("PORT", 8080)) 
    app.run(host='0.0.0.0', port=port)