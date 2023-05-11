# jwp-subway-path

## 도메인
### 역 (Station)
- `name`
### 구간 (Section)
- `startStation`, `endStation`, `distance`
### 노선 (Line)
- `name`, `color`, `List<Section>`

## API
### 역 (Station)
- [ ] 역 등록
- [ ] 역 제거
### 노선 (Line)
- [ ] 노선 조회 - 포함된 역을 순서대로
```json
{
  "id" : 1,
  "name": "5호선",
  "color": "PURPLE",
  "stations": [
    "길동역", "강동역", "천호역"
  ]
}
```
- [ ] 노선 목록 조회 - 포함된 역을 순서대로
```json
[
  {
    "id": 1,
    "name": "5호선",
    "color": "PURPLE",
    "stations": [
      "길동역", "강동역", "천호역"
    ]
  }, 
  {
    "id": 2,
    "name": "8호선",
    "color": "PINK",
    "stations": [
      "강동구청역", "천호역", "잠실역"
    ]
  }
]
```
- [ ] 노선 등록
- [ ] 노선 제거
