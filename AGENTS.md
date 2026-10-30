# Agent Operating Rules

Dự án: `SHOP_E` (Spring Boot 4.1.2, Java 21, Thymeleaf, MySQL).
Đọc trước: `.ai/00_project_brief.md`, rồi summary module liên quan trong `.ai/03_module_summaries/`.

## Context rules

1. Không đọc toàn bộ repo nếu chưa cần.
2. Dùng CodeGraph để tìm symbol, flow, callers/callees, impact trước khi mở file. `projectPath` là root repo này.
3. Khi sửa code, chỉ đọc file liên quan trực tiếp và file test liên quan.
4. Mọi đề xuất thay đổi phải nêu rõ:
   - Files affected
   - Reason
   - Risk
   - Tests required
   - Rollback plan

## Output rules

- BA: use case, actor, precondition, main flow, alternative flow, business rules. Thiếu dữ liệu thì ghi Open Questions, không suy diễn nghiệp vụ.
- SA: component, sequence, data flow, API, DB impact, NFR.
- Tester: test scenario, test data, expected result, priority, automation candidate.
- Dev: small PR plan, patch summary, test command.
- DevOps: env, build, deploy, monitoring, backup, rollback.

## Task rules

- Không sửa code khi task chưa rõ phạm vi.
- Mỗi thay đổi chỉ xử lý một use case hoặc một nhóm lỗi liên quan.
- Không refactor lan ra ngoài task.

## Safety rules

- Không tự ý xóa file.
- Không commit secrets. Không đưa `.env` thật vào prompt.
- Không chạy migration production nếu chưa có người duyệt.
- Không push thẳng `main`/`master`. Không tự merge.
- Auth, thanh toán, xóa dữ liệu bắt buộc có người duyệt trước khi sửa.
