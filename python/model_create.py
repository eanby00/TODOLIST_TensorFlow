from numpy.core.fromnumeric import shape
import pandas as pd
import tensorflow as tf

# 데이터 불러오기
data = pd.read_csv("./data.csv", header= None)

# 데이터 중 난이도가 0인 데이터 제거
data = data[data[0] != 0]
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

# 달성율 기준 생성
data["level_achievement"] = "higher"
data.loc[(data["achievement"] < data["achievement"].mean()), "level_achievement"] = "lower"
# print(data)

# 난이도 기준 생성
data["level_difficulty"] = "higher"
data.loc[(data["difficulty"] < data["difficulty"].mean()), "level_difficulty"] = "lower"
# print(data)

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
H_difficulty = tf.keras.layers.Dense(7, activation="swish")(X_difficulty)
H_difficulty = tf.keras.layers.Dense(7, activation="swish")(H_difficulty)
H_difficulty = tf.keras.layers.Dense(7, activation="swish")(H_difficulty)
Y_difficulty = tf.keras.layers.Dense(2, activation="softmax")(H_difficulty)
model_difficulty = tf.keras.models.Model(X_difficulty, Y_difficulty)
model_difficulty.compile(loss="categorical_crossentropy", metrics="accuracy")

# 데이터로 모델 학습
model_difficulty.fit(independent_difficulty, dependent_difficulty, epochs= 1000)

# 모델 테스트
# temp_difficulty = float(input("테스트할 난이도를 입력하시오: "))
print(model_difficulty.predict([77]))
print(data_difficulty["difficulty"].mean())

# 달성율 데이터 생성
# data__achievement = data[["achievement", "level_achievement"]]