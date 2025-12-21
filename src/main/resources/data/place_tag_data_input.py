import json
import pymysql

# RDS 연결 정보
connection = pymysql.connect(
    host='localhost', 
    port=3306,
    user='root',
    password='root',
    database='gt',
    charset='utf8mb4'
)

try:
    with connection.cursor() as cursor:
        # JSON 파일 로드
        with open('place_tag.json', 'r', encoding='utf-8') as f:
            data = json.load(f)
        
        # 배치 삽입
        insert_query = """
        INSERT INTO place_tags (id, type)
        VALUES (%s, %s)
        """
        
        for item in data:
            cursor.execute(insert_query, (
                int(item['id']),
                item['type'],
            ))
        
        connection.commit()
        print(f"✅ {len(data)}개 데이터 삽입 완료!")

except Exception as e:
    connection.rollback()
    print(f"❌ 오류 발생: {e}")

finally:
    connection.close()