import pandas as pd

# 데이터 불러오기
data = pd.read_csv("./data.csv", header= None)
# print(data)

# 데이터 중 오류 케이스 제거
data = data[data[0] >= data[1]]
data = data[data[0] != 0]
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
print("------------------------------------\n")
print(data["difficulty"].mean(), data["achievement"].mean())