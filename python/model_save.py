import tensorflow as tf

# 모델 텐서플로우 라이트로 변환 후 저장

converter_difficulty = tf.lite.TFLiteConverter.from_saved_model("./model_difficulty")
converter_difficulty.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model_difficulty = converter_difficulty.convert()
open("converted_model_difficulty.tflite", "wb").write(tflite_model_difficulty)

converter_achievement = tf.lite.TFLiteConverter.from_saved_model("./model_achievement")
converter_achievement.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model_achievement = converter_achievement.convert()
open("converted_model_achievement.tflite", "wb").write(tflite_model_achievement)