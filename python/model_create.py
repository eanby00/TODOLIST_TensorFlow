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

# 기준 생성
data["level"] = "higher"
data.loc[(data["achievement"] < data["achievement"].mean()), "level"] = "lower"
print(data)