export const login = async (data: any) =>
  await fetch("/backend/login", {
    method: 'POST',
    body: JSON.stringify(data),
    headers: { "Content-Type": "application/json" }
  })
