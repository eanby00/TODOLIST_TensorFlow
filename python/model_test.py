import tensorflow as tf

# 난이도 모델 테스트
model_difficulty = tf.keras.models.load_model("./model_difficulty")
temp_difficulty = float(input("테스트할 난이도를 입력하시오: "))
print(model_difficulty.predict([temp_difficulty]))

# 달성율 모델 테스트
model_achievement = tf.keras.models.load_model("./model_achievement")
temp_achievement = float(input("테스트할 달성율을 입력하시오: "))
print(model_achievement.predict([temp_achievement]))