from numpy.core.fromnumeric import shape
import pandas as pd
import tensorflow as tf

# 데이터 불러오기
data = pd.read_csv("./data.csv", header= None)
# print(data)

# 데이터 중 오류 케이스 제거
data = data[data[0] >= data[1]]
# print(data)

# 데이터프레임 타입 변환
data = data.astype("float")
# print(data.dtypes)

# 데이터프레임 칼럼 이름 변경
data.columns = ["difficulty", "achievement_temp"]
# print(data)

# 달성율 데이터 추가
data["achievement"] = data["achievement_temp"] / data["difficulty"]
# print(data)
data = data.fillna(0)

# 달성율 기준 생성
data["level_achievement"] = "higher"
data.loc[(data["achievement"] < data["achievement"].mean()), "level_achievement"] = "lower"
# print(data)

# 난이도 기준 생성
data["level_difficulty"] = "higher"
data.loc[(data["difficulty"] < data["difficulty"].mean()), "level_difficulty"] = "lower"
print(data)

# -----------------------------------------------------------------------------------------

# 난이도 데이터 생성
data_difficulty = data[["difficulty", "level_difficulty"]]
# print(data_difficulty)

# 난이도 데이터 원핫인코딩
data_difficulty = pd.get_dummies(data_difficulty)
# print(data_difficulty)

# 독립 변수, 종속 변수로 설정
independent_difficulty = data_difficulty[["difficulty"]]
dependent_difficulty = data_difficulty[["level_difficulty_higher", "level_difficulty_lower"]]
# print(independent_difficulty)
# print(dependent_difficulty)

# 난이도 모델 구조 생성
X_difficulty = tf.keras.layers.Input(shape=[1])
H_difficulty = tf.keras.layers.Dense(2, activation="swish")(X_difficulty)
H_difficulty = tf.keras.layers.Dense(4, activation="swish")(H_difficulty)
H_difficulty = tf.keras.layers.Dense(8, activation="swish")(H_difficulty)
H_difficulty = tf.keras.layers.Dense(16, activation="swish")(H_difficulty)
H_difficulty = tf.keras.layers.Dense(8, activation="swish")(H_difficulty)
H_difficulty = tf.keras.layers.Dense(4, activation="swish")(H_difficulty)
Y_difficulty = tf.keras.layers.Dense(2, activation="softmax")(H_difficulty)
model_difficulty = tf.keras.models.Model(X_difficulty, Y_difficulty)
model_difficulty.compile(loss="categorical_crossentropy", metrics="accuracy")

# 데이터로 모델 학습
model_difficulty.fit(independent_difficulty, dependent_difficulty, epochs= 10000)

# --------------------------------------------------------------------------------------

# 달성율 데이터 생성
data_achievement = data[["achievement", "level_achievement"]]

# 난이도 데이터 원핫인코딩
data_achievement = pd.get_dummies(data_achievement)
# print(data_achievement)

# 독립 변수, 종속 변수로 설정
independent_achievement = data_achievement[["achievement"]]
dependent_achievement = data_achievement[["level_achievement_higher", "level_achievement_lower"]]
# print(independent_achievement)
# print(dependent_achievement)

# 달성율 모델 구조 생성
X_achievement = tf.keras.layers.Input(shape=[1])
H_achievement = tf.keras.layers.Dense(2, activation="swish")(X_achievement)
H_achievement = tf.keras.layers.Dense(4, activation="swish")(H_achievement)
H_achievement = tf.keras.layers.Dense(8, activation="swish")(H_achievement)
H_achievement = tf.keras.layers.Dense(16, activation="swish")(H_achievement)
H_achievement = tf.keras.layers.Dense(8, activation="swish")(H_achievement)
H_achievement = tf.keras.layers.Dense(4, activation="swish")(H_achievement)
Y_achievement = tf.keras.layers.Dense(2, activation="softmax")(H_achievement)
model_achievement = tf.keras.models.Model(X_achievement, Y_achievement)
model_achievement.compile(loss="categorical_crossentropy", metrics="accuracy")

# 데이터로 모델 학습
model_achievement.fit(independent_achievement, dependent_achievement, epochs= 10000)

# --------------------------------------------------------------------------------------

# 모델 저장
model_difficulty.save("./model_difficulty")
model_achievement.save("./model_achievement")