INSERT INTO
    users(nickname, password, status, email, phone_number, web_site, profile_image)
VALUES
    ('홍길동', '$2a$10$P04N91fpnmwXfiK6JA/OiOE/P/9UDRn4mFn2sJpTWsG4bc1WDTKy.', '맛집 찾아 삼만리', 'gildong@naver.com', '010-1234-1234', '아직 연결된 외부 사이트가 없습니다.', '기본프로필 이미지 링크'),
    ('강호동', '$2a$10$REDzwMbjz2zj9laxqr.OpO.XTjogqRNFUDBi3CjpOjjY1ZUUBgI9a', '뭐든지 잘 먹습니다.', 'hodong@gmail.com', '010-1111-1234', 'https://www.naver.com', '기본프로필 이미지 링크'),
    ('유재석', '$2a$10$Ly4G8YbB2Y0j5zFnyuQ9XeV8b1ZfrU.7DsQpBJqzhiumNVmHRGOhW', '맛있는 집 추천 받아요!', 'jaesuk@naver.com', '010-1234-1111', '아직 연결된 외부 사이트가 없습니다.', '기본프로필 이미지 링크'),
    ('마동석', '$2a$10$QOTwD5aUTw5SqPLzBrKG4udn7X9/yNIFndk7g1lDEIxaX9DnyEVCO', '세상 모든 식당을 리뷰할 때까지', 'dongsuk@naver.com', '010-9876-1234', '아직 연결된 외부 사이트가 없습니다.', '기본프로필 이미지 링크'),
    ('이순신', '$2a$10$.lKPR9zG9OTv2WxpP5T03.h6BXZPd6qQP1/C6Q/wvL9vSX8Hi3Phy', '단게 먹고 싶네요.', 'sunsin@gmail.com', '010-1234-9876', '아직 연결된 외부 사이트가 없습니다.', '기본프로필 이미지 링크');

INSERT INTO
    authority(role, users)
VALUES
    ('ROLE_USER', 1),
    ('ROLE_USER', 2),
    ('ROLE_USER', 3),
    ('ROLE_USER', 4),
    ('ROLE_USER', 5);

insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강남구 대치동 996-16','한식','27531028','02-558-7905','중앙해장','http://place.map.kakao.com/27531028','서울 강남구 영동대로86길 17','127.065472540919','37.508273597184',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강남구 논현동 115-10','한식','27584230','02-515-3469','진미평양냉면','http://place.map.kakao.com/27584230','서울 강남구 학동로 305-3','127.036047158128','37.5161357904841',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강남구 신사동 569-31','간식','8746906','02-516-3643','압구정공주떡','http://place.map.kakao.com/8746906','서울 강남구 논현로161길 10','127.02746679415719','37.52356596669052',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강동구 성내동 442-12','간식','18310200','1588-5678','백년화편','http://place.map.kakao.com/18310200','서울 강동구 양재대로81길 32','127.13251425245822','37.5232515505377',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강동구 성내동 448-24','중식','12444560','02-470-2600','차이나린찐','http://place.map.kakao.com/12444560','서울 강동구 강동대로 217','127.130528004417','37.5233781006404',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강동구 천호동 454-15','양식','603985516','02-487-7999','로니로티 천호점','http://place.map.kakao.com/603985516','서울 강동구 천호대로 1027','127.12638533557136','37.53810676226715',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강북구 수유동 56-45','일식','16380488','02-2241-7558','다래함박스텍','http://place.map.kakao.com/16380488','서울 강북구 수유로 20-2','127.019032803713','37.6327573067632',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강북구 번동 470-1','한식','877502430','02-902-9915','진호횟집','http://place.map.kakao.com/877502430','서울 강북구 도봉로98길 33','127.03149836339257','37.641297778414796',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강북구 수유동 596-4','한식','8362137','02-902-6456','샘터마루','http://place.map.kakao.com/8362137','서울 강북구 4.19로12길 35','127.006082566071','37.6466446620218',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강서구 염창동 280-6','한식','9370927','02-3665-3930','유림 닭도리탕','http://place.map.kakao.com/9370927','서울 강서구 공항대로71길 5','126.87171977039051','37.547826497941145',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강서구 화곡동 782-12','한식','13561654','070-7641-2008','조연탄','http://place.map.kakao.com/13561654','서울 강서구 곰달래로60길 29','126.863295223301','37.5309034836946',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 강서구 마곡동 772-8','한식','294491415','0507-1386-8295','금고깃집 마곡본점','http://place.map.kakao.com/294491415','서울 강서구 마곡동로 61','126.83319727621857','37.56050944967595',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 관악구 신림동 546-1','일식','27508076','02-3281-3330','온정돈까스','http://place.map.kakao.com/27508076','서울 관악구 조원로 60','126.90910720206458','37.48354757120769',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 관악구 봉천동 1660-7','간식','16990385','02-889-5170','쟝블랑제리','http://place.map.kakao.com/16990385','서울 관악구 낙성대역길 8','126.961968234363','37.477157623199',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 관악구 신림동 10-518','한식','10122642','02-886-9233','강강술래 신림점','http://place.map.kakao.com/10122642','서울 관악구 남부순환로 1660','126.935032063852','37.4845810083297',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 광진구 구의동 26-15','한식','16649162','02-447-6540','원조할아버지손두부','http://place.map.kakao.com/16649162','서울 광진구 자양로 324','127.09416187008681','37.55405750063962',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 광진구 자양동 227-138','기사식당','7963415','02-457-5473','송림식당','http://place.map.kakao.com/7963415','서울 광진구 자양번영로 79','127.076404551637','37.5366882542409',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 광진구 구의동 52-17','분식','13319252','','신토불이떡볶이 본점','http://place.map.kakao.com/13319252','서울 광진구 자양로43길 42','127.09047521166055','37.55256562797821',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 구로구 가리봉동 127-5','중식','929624344','02-855-8488','월래순교자관','http://place.map.kakao.com/929624344','서울 구로구 디지털로19길 13','126.88983784572301','37.48028123854243',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 구로구 구로동 1124-69','한식','898970657','070-8624-1998','신림춘천집 구로디지털직영점','http://place.map.kakao.com/898970657','서울 구로구 디지털로32나길 17-23','126.899823896113','37.4840626496785',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 구로구 구로동 188-25','일식','26505507','02-6344-3782','스시메이진 구로점','http://place.map.kakao.com/26505507','서울 구로구 디지털로 300','126.89682602713681','37.48524940051567',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 금천구 시흥동 991','한식','8153541','02-808-1888','강강술래 시흥점','http://place.map.kakao.com/8153541','서울 금천구 시흥대로 193','126.90144238492762','37.45094555895512',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 금천구 시흥동 115-10','중식','11833906','02-803-3759','동흥관','http://place.map.kakao.com/11833906','서울 금천구 시흥대로63길 20','126.898583086671','37.4551319490809',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 금천구 가산동 371-37','한식','1954563179','010-9581-8981','가산물갈비&백년불고기','http://place.map.kakao.com/1954563179','서울 금천구 가산디지털1로 128','126.88366152357','37.477275859989',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 노원구 상계동 434-49','한식','1317959049','070-7543-4481','감동식당','http://place.map.kakao.com/1317959049','서울 노원구 한글비석로47길 58','127.06659520859755','37.66156431075787',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 노원구 상계동 1025-4','한식','18536783','02-935-9233','강강술래 상계지점','http://place.map.kakao.com/18536783','서울 노원구 동일로 1628','127.05603035404788','37.673843857521106',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 노원구 공릉동 740','한식','16414503','02-949-7331','경복식당','http://place.map.kakao.com/16414503','서울 노원구 공릉로39길 10','127.0786990932734','37.623772287025',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 도봉구 창동 337-1','한식','14701954','02-900-9800','하누소 창동본점','http://place.map.kakao.com/14701954','서울 도봉구 노해로 327','127.0448063295615','37.651519639360096',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 도봉구 방학동 436-9','한식','11003958','02-956-0843','대문','http://place.map.kakao.com/11003958','서울 도봉구 시루봉로 139-6','127.02752284948','37.6636161160514',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 도봉구 도봉동 600-4','한식','13091152','02-954-6292','무수옥','http://place.map.kakao.com/13091152','서울 도봉구 도봉로165길 15','127.0441642533523','37.67713476879158',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 동대문구 용두동 232-26','한식','907979960','02-923-1718','어머니대성집','http://place.map.kakao.com/907979960','서울 동대문구 왕산로11길 4','127.02858466713235','37.57741837425039',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 동대문구 용두동 119-20','한식','27175293','02-928-0231','나정순할매쭈꾸미','http://place.map.kakao.com/27175293','서울 동대문구 무학로 144','127.03069016915066','37.57707546781634',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 동대문구 전농동 602-9','한식','16531175','02-965-5838','서울뼈구이매운족발','http://place.map.kakao.com/16531175','서울 동대문구 왕산로 274-1','127.051278756793','37.5850349055577',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 동작구 사당동 138-4','분식','10587024','02-595-1629','애플하우스 이수','http://place.map.kakao.com/10587024','서울 동작구 동작대로27다길 29','126.98057475807556','37.4862749512195',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 동작구 신대방동 395-69','뷔페','2041419925','02-835-6274','쿠우쿠우 보라매공원점','http://place.map.kakao.com/2041419925','서울 동작구 보라매로5가길 16','126.924094036542','37.4912743811746',0);
insert into place(address_name,category_name,kakao_place_id,phone,place_name,place_url,road_address_name,x,y,average_rating) values('서울 동작구 사당동 1041-5','한식','21827385','02-3472-0108','조가네갑오징어 사당점','http://place.map.kakao.com/21827385','서울 동작구 남부순환로271길 24','126.98020384145781','37.477895507340364',0);

INSERT INTO REVIEW (AVERAGE_RATING, CONTENT, HYGIENE_RATING, KINDNESS_RATING, TASTE_RATING, KAKAO_PLACE_ID, USER_ID)
VALUES (3.3, '정말 맛있는 내장탕과 해장국집', 2.0, 3.0, 5.0, 27531028, 1);

INSERT INTO REVIEW (AVERAGE_RATING, CONTENT, HYGIENE_RATING, KINDNESS_RATING, TASTE_RATING, KAKAO_PLACE_ID, USER_ID)
VALUES (5, '한우 양선지 해장국이랑 한우 내장탕 먹었어요>< 잡내없고 맛도링', 5, 5, 5, 27531028, 2);

INSERT INTO REVIEW (AVERAGE_RATING, CONTENT, HYGIENE_RATING, KINDNESS_RATING, TASTE_RATING, KAKAO_PLACE_ID, USER_ID)
VALUES (3.7, '역시 중앙해장 간만에 오니 좋으네요. 근데 웨이팅이 생김', 5, 3, 3, 27531028, 4);

INSERT INTO REVIEW (AVERAGE_RATING, CONTENT, HYGIENE_RATING, KINDNESS_RATING, TASTE_RATING, KAKAO_PLACE_ID, USER_ID)
VALUES (5, '하 평양냉면 1티어 진미', 5, 5, 5, 27584230, 1);

INSERT INTO REVIEW (AVERAGE_RATING, CONTENT, HYGIENE_RATING, KINDNESS_RATING, TASTE_RATING, KAKAO_PLACE_ID, USER_ID)
VALUES (4, '제 최애 평양냉면집입니다.', 4, 4, 4, 27584230, 3);

INSERT INTO REVIEW (AVERAGE_RATING, CONTENT, HYGIENE_RATING, KINDNESS_RATING, TASTE_RATING, KAKAO_PLACE_ID, USER_ID)
VALUES (3.7, '웨이팅이 꽤 긴편인데 그럴만한 가치가 있는곳이긴 해요.', 3, 4, 4, 929624344, 1);

INSERT INTO REVIEW (AVERAGE_RATING, CONTENT, HYGIENE_RATING, KINDNESS_RATING, TASTE_RATING, KAKAO_PLACE_ID, USER_ID)
VALUES (4.3, '디진다돈까쓰!!!', 5, 4, 4, 27508076, 2);

INSERT INTO REVIEW (AVERAGE_RATING, CONTENT, HYGIENE_RATING, KINDNESS_RATING, TASTE_RATING, KAKAO_PLACE_ID, USER_ID)
VALUES (4, '주말일찍 가니까 웨티이없고 맛있게 먹었네요~', 4, 4, 4, 10587024, 5);